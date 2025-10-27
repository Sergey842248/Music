/*
 * Copyright (c) 2020 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.fragments.folder

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.webkit.MimeTypeMap
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.os.BundleCompat
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore.Companion.accentColor
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.SongFileAdapter
import code.name.monkey.retromusic.adapter.Storage
import code.name.monkey.retromusic.adapter.StorageAdapter
import code.name.monkey.retromusic.adapter.StorageClickListener
import code.name.monkey.retromusic.adapter.TreeViewAdapter
import code.name.monkey.retromusic.databinding.FragmentFolderBinding
import code.name.monkey.retromusic.extensions.dip
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.extensions.textColorPrimary
import code.name.monkey.retromusic.extensions.textColorSecondary
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote.openQueueKeepShuffleMode
import code.name.monkey.retromusic.helper.menu.SongMenuHelper
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper
import code.name.monkey.retromusic.interfaces.ICallbacks
import code.name.monkey.retromusic.interfaces.IMainActivityFragmentCallbacks
import code.name.monkey.retromusic.interfaces.IScrollHelper
import code.name.monkey.retromusic.misc.UpdateToastMediaScannerCompletionListener
import code.name.monkey.retromusic.misc.WrappedAsyncTaskLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.BlacklistStore
import code.name.monkey.retromusic.util.FileUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.PreferenceUtil.startDirectory
import code.name.monkey.retromusic.util.ThemedFastScroller.create
import code.name.monkey.retromusic.util.getExternalStorageDirectory
import code.name.monkey.retromusic.util.getExternalStoragePublicDirectory
import code.name.monkey.retromusic.views.BreadCrumbLayout
import code.name.monkey.retromusic.views.BreadCrumbLayout.Crumb
import code.name.monkey.retromusic.views.BreadCrumbLayout.SelectionCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class FoldersFragment : AbsMainActivityFragment(R.layout.fragment_folder),
    IMainActivityFragmentCallbacks, SelectionCallback, ICallbacks,
    LoaderManager.LoaderCallbacks<List<Song>>, StorageClickListener, IScrollHelper,
    TreeViewAdapter.OnFolderClickListener {
    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    val toolbar: Toolbar get() = binding.appBarLayout.toolbar

    private var songFileAdapter: SongFileAdapter? = null
    private var treeViewAdapter: TreeViewAdapter? = null
    private var storageAdapter: StorageAdapter? = null
    private val fileComparator = Comparator { lhs: File, rhs: File ->
        if (lhs.isDirectory && !rhs.isDirectory) {
            return@Comparator -1
        } else if (!lhs.isDirectory && rhs.isDirectory) {
            return@Comparator 1
        } else {
            return@Comparator lhs.name.compareTo(rhs.name, ignoreCase = true)
        }
    }
    private var storageItems = ArrayList<Storage>()

    override fun onFolderClick(file: File) {
        onFileSelected(file)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFolderBinding.bind(view)
        mainActivity.addMusicServiceEventListener(libraryViewModel)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = null
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()

        setUpBreadCrumbs()
        checkForMargins()
        setUpRecyclerView()
        setUpAdapter()
        setUpTitle()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!handleBackPress()) {
                        remove()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            })
        if (savedInstanceState == null) {
            switchToFileAdapter()
            setCrumb(
                Crumb(
                    FileUtil.safeGetCanonicalFile(startDirectory)
                ),
                true
            )
        } else {
            binding.breadCrumbs.restoreFromStateWrapper(
                BundleCompat.getParcelable(
                    savedInstanceState,
                    CRUMBS,
                    BreadCrumbLayout.SavedStateWrapper::class.java
                )
            )
            LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding != null) {
            outState.putParcelable(CRUMBS, binding.breadCrumbs.stateWrapper)
        }
    }

    private fun setUpTitle() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_search, null, navOptions)
        }
        binding.appBarLayout.title = resources.getString(R.string.folders)
    }

    override fun onPause() {
        super.onPause()
        saveScrollPosition()
        songFileAdapter?.actionMode?.finish() // Assuming actionMode is only relevant for SongFileAdapter
    }

    override fun handleBackPress(): Boolean {
        if (binding.breadCrumbs.popHistory()) {
            setCrumb(binding.breadCrumbs.lastHistory(), false)
            return true
        }
        return false
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Song>> {
        return AsyncFileLoader(this)
    }

    override fun onCrumbSelection(crumb: Crumb, index: Int) {
        setCrumb(crumb, true)
    }

    override fun onFileMenuClicked(file: File, view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        if (file.isDirectory) {
            popupMenu.inflate(R.menu.menu_item_directory)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (val itemId = item.itemId) {
                    R.id.action_play_next, R.id.action_add_to_current_playing, R.id.action_add_to_playlist, R.id.action_delete_from_device -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            listSongs(
                                requireContext(),
                                listOf(file),
                                AUDIO_FILE_FILTER,
                                fileComparator
                            ) { songs ->
                                if (songs.isNotEmpty()) {
                                    SongsMenuHelper.handleMenuClick(
                                        requireActivity(), songs, itemId
                                    )
                                }
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_add_to_blacklist -> {
                        BlacklistStore.getInstance(requireContext()).addPath(file)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_set_as_start_directory -> {
                        startDirectory = file
                        showToast(
                            String.format(getString(R.string.new_start_directory), file.path)
                        )
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_scan -> {
                        lifecycleScope.launch {
                            listPaths(file, AUDIO_FILE_FILTER) { paths -> scanPaths(paths) }
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
        } else {
            popupMenu.inflate(R.menu.menu_item_file)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (val itemId = item.itemId) {
                    R.id.action_play_next, R.id.action_add_to_current_playing, R.id.action_add_to_playlist, R.id.action_go_to_album, R.id.action_go_to_artist, R.id.action_share, R.id.action_tag_editor, R.id.action_details, R.id.action_set_as_ringtone, R.id.action_delete_from_device -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            listSongs(
                                requireContext(),
                                listOf(file),
                                AUDIO_FILE_FILTER,
                                fileComparator
                            ) { songs ->
                                if (songs.isNotEmpty()) {
                                    val song = songs.first()
                                    SongMenuHelper.handleMenuClick(
                                        requireActivity(), song, itemId
                                    )
                                }
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_scan -> {
                        lifecycleScope.launch {
                            listPaths(file, AUDIO_FILE_FILTER) { paths -> scanPaths(paths) }
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
        }
        try {
            val field = popupMenu.javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(popupMenu)
            val showIcons = menuPopupHelper.javaClass.getDeclaredMethod(
                "setForceShowIcon",
                Boolean::class.javaPrimitiveType
            )
            showIcons.invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        popupMenu.show()
    }

    override fun onFileSelected(file: File) {
        var mFile = file
        mFile = tryGetCanonicalFile(mFile) // important as we compare the path value later
        if (mFile.isDirectory) {
            setCrumb(Crumb(mFile), true)
        } else {
            val fileFilter = FileFilter { pathname: File ->
                !pathname.isDirectory && AUDIO_FILE_FILTER.accept(pathname)
            }
            lifecycleScope.launch(Dispatchers.IO) {
                listSongs(
                    requireContext(),
                    listOf(mFile.parentFile),
                    fileFilter,
                    fileComparator
                ) { songs ->
                    if (songs.isNotEmpty()) {
                        var startIndex = -1
                        for (i in songs.indices) {
                            if (mFile.path
                                == songs[i].data
                            ) { // path is already canonical here
                                startIndex = i
                                break
                            }
                        }
                        if (startIndex > -1) {
                            openQueueKeepShuffleMode(songs, startIndex, true)
                        } else {
                            Snackbar.make(
                                mainActivity.slidingPanel,
                                String.format(
                                    getString(R.string.not_listed_in_media_store), mFile.name

                                ).parseAsHtml(),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(
                                    R.string.action_scan
                                ) {
                                    lifecycleScope.launch {
                                        listPaths(mFile, AUDIO_FILE_FILTER) { paths ->
                                            scanPaths(
                                                paths
                                            )
                                        }
                                    }
                                }
                                .setActionTextColor(accentColor(requireActivity()))
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<Song>>, data: List<Song>) {
        updateAdapter(data)
        checkIsEmpty() // Call checkIsEmpty after data is loaded for songFileAdapter
    }

    override fun onLoaderReset(loader: Loader<List<Song>>) {
        updateAdapter(LinkedList())
    }

    override fun onMultipleItemAction(item: MenuItem, files: ArrayList<File>) {
        val itemId = item.itemId

        lifecycleScope.launch(Dispatchers.IO) {
            listSongs(requireContext(), files, AUDIO_FILE_FILTER, fileComparator) { songs ->
                if (songs.isNotEmpty()) {
                    SongsMenuHelper.handleMenuClick(
                        requireActivity(), songs, itemId
                    )
                }
            }
        }
    }

    override fun onPrepareMenu(menu: Menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        try {
            val menuBuilder = menu.javaClass.getDeclaredMethod(
                "setOptionalIconsVisible",
                Boolean::class.javaPrimitiveType
            )
            menuBuilder.isAccessible = true
            menuBuilder.invoke(menu, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        menu.add(0, R.id.action_scan, 0, R.string.scan_media)
            .setIcon(R.drawable.ic_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.add(0, R.id.action_go_to_start_directory, 1, R.string.action_go_to_start_directory)
            .setIcon(R.drawable.ic_home)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.add(0, R.id.action_set_as_start_directory, 2, R.string.action_set_as_start_directory)
            .setIcon(R.drawable.ic_home)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.add(0, R.id.action_settings, 3, R.string.action_settings)
            .setIcon(R.drawable.ic_settings)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.add(0, R.id.action_toggle_folder_view, 4, R.string.action_toggle_folder_view)
            .setIcon(R.drawable.ic_tree_view) // Assuming an icon for tree view exists or will be added
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
        menu.removeItem(R.id.action_grid_size)
        menu.removeItem(R.id.action_layout_type)
        menu.removeItem(R.id.action_sort_order)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(), toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(
                toolbar
            )
        )
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_go_to_start_directory -> {
                setCrumb(
                    Crumb(
                        tryGetCanonicalFile(startDirectory)
                    ),
                    true
                )
                return true
            }

            R.id.action_set_as_start_directory -> {
                val crumb = activeCrumb
                if (crumb != null) {
                    startDirectory = crumb.file
                    showToast(
                        String.format(getString(R.string.new_start_directory), crumb.file.path)
                    )
                }
                return true
            }

            R.id.action_scan -> {
                val crumb = activeCrumb
                if (crumb != null) {
                    lifecycleScope.launch {
                        listPaths(crumb.file, AUDIO_FILE_FILTER) { paths -> scanPaths(paths) }
                    }
                }
                return true
            }

            R.id.action_settings -> {
                findNavController().navigate(
                    R.id.settings_fragment,
                    null,
                    navOptions
                )
                return true
            }

            R.id.action_toggle_folder_view -> {
                PreferenceUtil.folderViewType = if (PreferenceUtil.folderViewType == 0) 1 else 0
                updateFolderView()
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        checkForMargins()
    }

    private fun checkForMargins() {
        if (mainActivity.isBottomNavVisible) {
            binding.recyclerView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = dip(R.dimen.bottom_nav_height)
            }
        }
    }

    private fun checkIsEmpty() {
        if (_binding != null) {
            binding.empty.isVisible = when (PreferenceUtil.folderViewType) {
                0 -> songFileAdapter?.itemCount == 0
                1 -> treeViewAdapter?.itemCount == 0
                else -> true
            }
        }
    }

    private val activeCrumb: Crumb?
        get() = if (_binding != null) {
            if (binding.breadCrumbs.size() > 0) binding.breadCrumbs.getCrumb(binding.breadCrumbs.activeIndex) else null
        } else null

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun saveScrollPosition() {
        activeCrumb?.scrollPosition =
            (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }


    private fun scanPaths(toBeScanned: Array<String?>) {
        if (activity == null) {
            return
        }
        if (toBeScanned.isEmpty()) {
            showToast(R.string.nothing_to_scan)
        } else {
            MediaScannerConnection.scanFile(
                requireContext(),
                toBeScanned,
                null,
                UpdateToastMediaScannerCompletionListener(activity, listOf(*toBeScanned))
            )
        }
    }

    private fun setCrumb(crumb: Crumb?, addToHistory: Boolean) {
        if (crumb == null) {
            return
        }
        val path = crumb.file.path
        if (path == "/" || path == "/storage" || path == "/storage/emulated") {
            switchToStorageAdapter()
        } else {
            saveScrollPosition()
            binding.breadCrumbs.setActiveOrAdd(crumb, false)
            if (addToHistory) {
                binding.breadCrumbs.addHistory(crumb)
            }
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this)
        }
    }

    private fun setUpAdapter() {
        updateFolderView()
    }

    private fun updateFolderView() {
        if (PreferenceUtil.folderViewType == 0) {
            switchToFileAdapter()
        } else {
            switchToTreeAdapter()
        }
    }

    private fun setUpBreadCrumbs() {
        binding.breadCrumbs.setActivatedContentColor(
            textColorPrimary()
        )
        binding.breadCrumbs.setDeactivatedContentColor(
            textColorSecondary()

        )
        binding.breadCrumbs.setCallback(this)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        create(
            binding.recyclerView
        )
    }

    private fun updateAdapter(songs: List<Song>) {
        if (PreferenceUtil.folderViewType == 0) {
            songFileAdapter?.swapDataSet(songs)
        } else {
            // Tree view adapter doesn't use songs directly, it uses TreeItems
            // We need to rebuild the tree view data
            val crumb = activeCrumb
            if (crumb != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val treeItems = buildTreeItems(crumb.file, 0)
                    withContext(Dispatchers.Main) {
                        treeViewAdapter?.submitList(treeItems)
                        (binding.recyclerView.layoutManager as LinearLayoutManager)
                            .scrollToPositionWithOffset(crumb.scrollPosition, 0)
                        checkIsEmpty() // Call checkIsEmpty after data is submitted
                    }
                }
            }
        }
        val crumb = activeCrumb
        if (crumb != null) {
            (binding.recyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(crumb.scrollPosition, 0)
        }
        checkIsEmpty() // Also call here for general updates
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun listPaths(
        file: File,
        fileFilter: FileFilter,
        doOnPathListed: (paths: Array<String?>) -> Unit,
    ) {
        val paths = try {
            val paths: Array<String?>
            if (file.isDirectory) {
                val files = FileUtil.listFilesDeep(file, fileFilter)
                paths = arrayOfNulls(files.size)
                for (i in files.indices) {
                    val f = files[i]
                    paths[i] = FileUtil.safeGetCanonicalPath(f)
                }
            } else {
                paths = arrayOfNulls(1)
                paths[0] = file.path
            }
            paths
        } catch (e: Exception) {
            e.printStackTrace()
            arrayOf()
        }
        withContext(Dispatchers.Main) {
            doOnPathListed(paths)
        }
    }

    private class AsyncFileLoader(foldersFragment: FoldersFragment) :
        WrappedAsyncTaskLoader<List<Song>>(foldersFragment.requireActivity()) {
        private val fragmentWeakReference: WeakReference<FoldersFragment> =
            WeakReference(foldersFragment)

        override fun loadInBackground(): List<Song> {
            val foldersFragment = fragmentWeakReference.get()
            var directory: File? = null
            if (foldersFragment != null) {
                val crumb = foldersFragment.activeCrumb
                if (crumb != null) {
                    directory = crumb.file
                }
            }
            if (directory == null) {
                return LinkedList()
            }

            val files = FileUtil.listFiles(directory, AUDIO_FILE_FILTER)
            foldersFragment?.fileComparator?.let { Collections.sort(files, it) }

            val songs = LinkedList<Song>()
            for (file in files) {
                songs.add(
                    if (file.isDirectory) {
                        Song(
                            id = -file.absolutePath.hashCode().toLong(),
                            title = file.name,
                            trackNumber = 0,
                            year = null,
                            duration = 0L,
                            data = file.absolutePath,
                            dateModified = file.lastModified(),
                            albumId = -1,
                            albumName = "Folder",
                            artistId = -1,
                            artistName = "Folder",
                            composer = null,
                            albumArtist = null
                        )
                    } else {
                        Song(
                            id = -file.absolutePath.hashCode().toLong(),
                            title = FileUtil.stripExtension(file.name),
                            trackNumber = 0,
                            year = null,
                            duration = 0L,
                            data = file.absolutePath,
                            dateModified = file.lastModified(),
                            albumId = -1,
                            albumName = "Unknown Album",
                            artistId = -1,
                            artistName = "Unknown Artist",
                            composer = null,
                            albumArtist = null
                        )
                    }
                )
            }
            return songs
        }
    }

    private suspend fun listSongs(
        context: Context,
        files: List<File?>,
        fileFilter: FileFilter,
        fileComparator: Comparator<File>,
        doOnSongsListed: (songs: List<Song>) -> Unit,
    ) {
        val songs = try {
            val fileList = FileUtil.listFilesDeep(files, fileFilter)
            Collections.sort(fileList, fileComparator)
            FileUtil.matchFilesWithMediaStore(context, fileList)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
        withContext(Dispatchers.Main) {
            doOnSongsListed(songs)
        }
    }

    override fun onStorageClicked(storage: Storage) {
        switchToFileAdapter()
        setCrumb(
            Crumb(
                FileUtil.safeGetCanonicalFile(storage.file)
            ),
            true
        )
    }

    override fun scrollToTop() {
        binding.recyclerView.scrollToPosition(0)
        binding.appBarLayout.setExpanded(true, true)
    }

    private fun switchToFileAdapter() {
        songFileAdapter = SongFileAdapter(mainActivity, LinkedList(), R.layout.item_list, this)
        songFileAdapter!!.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    checkIsEmpty()
                }
            })
        binding.recyclerView.adapter = songFileAdapter
        val crumb = activeCrumb
        if (crumb != null) {
            LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this)
        }
        checkIsEmpty()
        showToast(R.string.standard_view_enabled)
    }

    private fun switchToTreeAdapter() {
        treeViewAdapter = TreeViewAdapter(this)
        treeViewAdapter!!.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    checkIsEmpty()
                }
            })
        binding.recyclerView.adapter = treeViewAdapter
        showToast(R.string.tree_view_enabled)
        val crumb = activeCrumb
        if (crumb != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                val treeItems = buildTreeItems(crumb.file, 0)
                withContext(Dispatchers.Main) {
                    treeViewAdapter?.submitList(treeItems)
                    checkIsEmpty() // Call checkIsEmpty after data is submitted
                }
            }
        } else {
            checkIsEmpty() // If no crumb, still check if empty
        }
    }

    private fun switchToStorageAdapter() {
        storageItems = FileUtil.listRoots()
        storageAdapter = StorageAdapter(storageItems, this)
        binding.recyclerView.adapter = storageAdapter
        binding.breadCrumbs.clearCrumbs()
    }

    companion object {
        val TAG: String = FoldersFragment::class.java.simpleName
        val AUDIO_FILE_FILTER = FileFilter { file: File ->
            (!file.isHidden
                    && (file.isDirectory
                    || FileUtil.fileIsMimeType(file, "audio/*", MimeTypeMap.getSingleton())
                    || FileUtil.fileIsMimeType(
                file,
                "application/opus",
                MimeTypeMap.getSingleton()
            )
                    || FileUtil.fileIsMimeType(
                file,
                "application/ogg",
                MimeTypeMap.getSingleton()
            )))
        }
        private const val CRUMBS = "crumbs"
        private const val LOADER_ID = 5

        private fun buildTreeItems(rootFile: File, depth: Int): List<TreeViewAdapter.TreeItem> {
            if (depth > 10) return emptyList()  // Prevent deep recursion
            val treeItems = mutableListOf<TreeViewAdapter.TreeItem>()
            val files = rootFile.listFiles(FileFilter { !it.isHidden && it.isDirectory })?.sortedWith(Comparator { lhs, rhs ->
                lhs.name.compareTo(rhs.name, ignoreCase = true)
            }) ?: emptyList()

            for (file in files) {
                treeItems.add(TreeViewAdapter.TreeItem(file, depth))
                if (file.isDirectory) {
                    treeItems.addAll(buildTreeItems(file, depth + 1))
                }
            }
            return treeItems
        }

        // root
        val defaultStartDirectory: File
            get() {
                val musicDir =
                    getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                val externalStorage = getExternalStorageDirectory()

                val startFolder = when {
                    musicDir.exists() && musicDir.isDirectory && musicDir.listFiles() != null -> musicDir
                    externalStorage.exists() && externalStorage.isDirectory && externalStorage.listFiles() != null -> externalStorage
                    else -> File("/") // root
                }

                return startFolder
            }

        private fun tryGetCanonicalFile(file: File): File {
            return try {
                file.canonicalFile
            } catch (e: IOException) {
                e.printStackTrace()
                file
            }
        }
    }
}
