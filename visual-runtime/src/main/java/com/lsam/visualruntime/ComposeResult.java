package com.lsam.visualruntime;

import java.io.File;

public class ComposeResult {
    public final File outputFile;
    public final String manifestSha256;
    public final boolean fromCache;

    public ComposeResult(File outputFile, String manifestSha256, boolean fromCache) {
        this.outputFile = outputFile;
        this.manifestSha256 = manifestSha256;
        this.fromCache = fromCache;
    }
}
