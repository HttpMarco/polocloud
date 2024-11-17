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

        for (int i = 0; i < Math.min(currentNumbers.length, latestNumbers.length); i++) {
            int currentNum = Integer.parseInt(currentNumbers[i]);
            int latestNum = Integer.parseInt(latestNumbers[i]);

            if (latestNum > currentNum) {
                return true; // Latest version is newer
            } else if (latestNum < currentNum) {
                return false; // Current version is newer
            }
        }

        // If major, minor, and patch are the same, check for pre-release suffix (like alpha, beta)
        return comparePreReleaseTags(currentParts.length > 1 ? currentVersion.split(currentParts[0] + "-")[1] : null,
                latestParts.length > 1 ? latestVersion.split(latestParts[0] + "-")[1] : null);
    }

    private boolean comparePreReleaseTags(String currentTag, String latestTag) {
        if (currentTag == null && latestTag != null) {
            return false;
        } else if (currentTag != null && latestTag == null) {
            return true;
        }

        var currentTagParts = currentTag.split("-");
        var latestTagParts = latestTag.split("-");

        int comparison = currentTagParts[0].compareTo(latestTagParts[0]);
        if (comparison < 0) {
            return true; // "alpha" is older than "beta"
        } else if (comparison > 0) {
            return false; // "beta" is newer than "alpha"
        }

        int currentNumber = currentTagParts.length > 1 ? Integer.parseInt(currentTagParts[1]) : 0;
        int latestNumber = latestTagParts.length > 1 ? Integer.parseInt(latestTagParts[1]) : 0;

        return latestNumber > currentNumber;
    }
}
