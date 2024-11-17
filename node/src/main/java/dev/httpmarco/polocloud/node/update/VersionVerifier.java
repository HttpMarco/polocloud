package dev.httpmarco.polocloud.node.update;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
public final class VersionVerifier {

    public boolean isNewerVersion(String currentVersion, String latestVersion) {
        var currentParts = currentVersion.split("-");
        var latestParts = latestVersion.split("-");

        var currentNumbers = currentParts[0].split("\\.");
        var latestNumbers = latestParts[0].split("\\.");

        for (int i = 0; i < Math.max(currentNumbers.length, latestNumbers.length); i++) {
            int currentNum = i < currentNumbers.length ? Integer.parseInt(currentNumbers[i]) : 0;
            int latestNum = i < latestNumbers.length ? Integer.parseInt(latestNumbers[i]) : 0;

            if (latestNum > currentNum) {
                return true; // Latest version is newer
            } else if (latestNum < currentNum) {
                return false; // Current version is newer
            }
        }

        // If major, minor, and patch are the same, check for pre-release suffix (like alpha, beta)
        return comparePreReleaseTags(currentParts.length > 1 ? currentParts[1] : null,
                latestParts.length > 1 ? latestParts[1] : null);
    }

    private boolean comparePreReleaseTags(String currentTag, String latestTag) {
        if (currentTag == null && latestTag != null) {
            return true; // A version with a tag (e.g., alpha) is considered older than no tag
        } else if (currentTag != null && latestTag == null) {
            return false; // A version without a tag is newer
        } else if (currentTag == null) {
            return false; // Both versions are equal
        }

        // Compare tags alphabetically (e.g., "alpha" < "beta")
        int comparison = currentTag.compareTo(latestTag);
        return comparison < 0;
    }
}
