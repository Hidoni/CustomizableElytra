// LLM generated util to parse the Forge/NeoForge version range into a Fabric compatible one
class VersionUtils {
    /**
     * Converts a Minecraft version range (e.g., "[1.16,1.17)") to a SemVer range string (e.g., ">=1.16.0 <1.17.0").
     *
     * @param range The version range string, e.g., "[26.1,26.2)", "[1.20,]" or "[1.20]".
     * @return A SemVer compatible range string.
     */
    static String convertMcRangeToSemVer(String range) {
        if (!range) return ""

        // Define the comparison operators based on interval notation
        // '[' or ']' = Inclusive, '(' or ')' = Exclusive
        boolean startInclusive = range.startsWith("[")
        boolean endInclusive = range.endsWith("]")

        // Remove the brackets and split by comma
        // Pattern allows for optional whitespace around the comma
        def content = range.substring(1, range.length() - 1)
        def parts = content.split(/\s*,\s*/)

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: '$range'. Expected format: [min,max]")
        }

        def rawMin = parts[0]
        def rawMax = parts[1]

        // Determine operators
        // Note: Empty strings for min/max (e.g. "[,1.0]") imply unbounded,
        // but this implementation focuses on bounded ranges like the example.
        def startOp = startInclusive ? ">=" : ">"
        def endOp = endInclusive ? "<=" : "<"

        // Normalize versions to Semantic Versioning (MAJOR.MINOR.PATCH)
        def minSemVer = normalizeToSemVer(rawMin)
        def maxSemVer = normalizeToSemVer(rawMax)

        // Construct the range string
        // If rawMax is empty (unbounded upper), we typically omit the end operator,
        // but for strict conversion we format what we have.
        // Here we handle the standard case where two bounds are present.
        def rangeParts = []
        if (rawMin) rangeParts << "$startOp$minSemVer"
        if (rawMax) rangeParts << "$endOp$maxSemVer"

        return rangeParts.join(" ")
    }

/**
 * Normalizes a version string to SemVer format (MAJOR.MINOR.PATCH).
 * Pads with ".0" if parts are missing.
 * e.g., "26.1" -> "26.1.0", "1" -> "1.0.0"
 */
    static String normalizeToSemVer(String version) {
        if (!version) return ""

        // Handle cases where version might just be a number like "1"
        def segments = version.split(/\./)

        switch (segments.length) {
            case 1:
                return "${segments[0]}.0.0"
            case 2:
                return "${segments[0]}.${segments[1]}.0"
            case 3:
                // Ensure they are valid numbers if possible, otherwise return as is (e.g. pre-releases)
                return version
            default:
                // If more than 3 parts (e.g. 1.2.3.4), SemVer spec usually treats 4th as build metadata or invalid.
                // We will return the first 3 parts for strict SemVer compliance.
                return "${segments[0]}.${segments[1]}.${segments[2]}"
        }
    }
}
