package code.name.monkey.retromusic.fragments.settings

import androidx.`annotation`.CheckResult
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import code.name.monkey.retromusic.R

public class PersonalizeSettingsFragmentDirections private constructor() {
  public companion object {
    @CheckResult
    public fun actionPersonalizeSettingsFragmentToNowPlayingMetadataPreferenceDialog(): NavDirections = ActionOnlyNavDirections(R.id.action_personalizeSettingsFragment_to_nowPlayingMetadataPreferenceDialog)
  }
}
