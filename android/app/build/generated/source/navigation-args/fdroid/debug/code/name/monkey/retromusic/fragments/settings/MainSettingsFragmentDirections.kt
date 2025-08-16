package code.name.monkey.retromusic.fragments.settings

import androidx.`annotation`.CheckResult
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import code.name.monkey.retromusic.R

public class MainSettingsFragmentDirections private constructor() {
  public companion object {
    @CheckResult
    public fun actionMainSettingsFragmentToThemeSettingsFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_themeSettingsFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToImageSettingFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_imageSettingFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToNowPlayingSettingsFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_nowPlayingSettingsFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToAudioSettings(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_audioSettings)

    @CheckResult
    public fun actionMainSettingsFragmentToOtherSettingsFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_otherSettingsFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToPersonalizeSettingsFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_personalizeSettingsFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToNotificationSettingsFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_notificationSettingsFragment)

    @CheckResult
    public fun actionMainSettingsFragmentToAboutActivity(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_aboutActivity)

    @CheckResult
    public fun actionMainSettingsFragmentToBackupFragment(): NavDirections = ActionOnlyNavDirections(R.id.action_mainSettingsFragment_to_backupFragment)
  }
}
