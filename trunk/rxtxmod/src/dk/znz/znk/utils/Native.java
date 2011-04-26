package dk.znz.znk.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Native {

    public static void loadNativeLibrary(String nativeName, String nativeResourcesRoot, String nativeBootPath) {
        String _bootPath = System.getProperty(nativeBootPath);
        if (_bootPath != null) {
            String[] dirs = _bootPath.split(File.pathSeparator);
            for (int i = 0; i < dirs.length; ++i) {
                String path = new File(new File(dirs[i]), System.mapLibraryName(nativeName)).getAbsolutePath();
                try {
                    System.load(path);
                    return;
                } catch (UnsatisfiedLinkError ex) {
                }
                if (Platform.isMac()) {
                    String orig, ext;
                    if (path.endsWith("dylib")) {
                        orig = "dylib";
                        ext = "jnilib";
                    } else {
                        orig = "jnilib";
                        ext = "dylib";
                    }
                    try {
                        path = path.substring(0, path.lastIndexOf(orig)) + ext;
                        System.load(path);
                        return;
                    } catch (UnsatisfiedLinkError ex) {
                    }
                }
            }
        }
        try {
            System.loadLibrary(nativeName);
        }
        catch(UnsatisfiedLinkError e) {
            loadNativeLibraryFromJar(nativeName, nativeResourcesRoot);
        }
    }
	
    private static void loadNativeLibraryFromJar(String nativeName, String nativeResourcesRoot) {
        String libname = System.mapLibraryName(nativeName);
        String arch = System.getProperty("os.arch");
        String name = System.getProperty("os.name");
        String resourceName = getNativeLibraryResourcePath(Platform.getOSType(), arch, name, nativeResourcesRoot) + "/" + libname;
        URL url = Native.class.getResource(resourceName);
        // Add an ugly hack for OpenJDK (soylatte) - JNI libs use the usual
        // .dylib extension 
        if (url == null && Platform.isMac()
            && resourceName.endsWith(".dylib")) {
            resourceName = resourceName.substring(0, resourceName.lastIndexOf(".dylib")) + ".jnilib";
            url = Native.class.getResource(resourceName);
        }
        if (url == null) {
            throw new UnsatisfiedLinkError(nativeName + " (" + resourceName 
                                           + ") not found in resource path");
        }
    
        File lib = null;
        if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                lib = new File(new URI(url.toString()));
            }
            catch(URISyntaxException e) {
                lib = new File(url.getPath());
            }
            if (!lib.exists()) {
                throw new Error("File URL " + url + " could not be properly decoded");
            }
        }
        else {
            InputStream is = Native.class.getResourceAsStream(resourceName);
            if (is == null) {
                throw new Error("Can't obtain " + nativeName + " InputStream");
            }
            
            FileOutputStream fos = null;
            try {
                // Suffix is required on windows, or library fails to load
                // Let Java pick the suffix, except on windows, to avoid
                // problems with Web Start.
                lib = File.createTempFile("jna", Platform.isWindows()?".dll":null);
                lib.deleteOnExit();
                ClassLoader cl = Native.class.getClassLoader();
                if (Platform.deleteNativeLibraryAfterVMExit()
                    && (cl == null
                        || cl.equals(ClassLoader.getSystemClassLoader()))) {
                    Runtime.getRuntime().addShutdownHook(new DeleteNativeLibrary(lib));
                }
                fos = new FileOutputStream(lib);
                int count;
                byte[] buf = new byte[1024];
                while ((count = is.read(buf, 0, buf.length)) > 0) {
                    fos.write(buf, 0, count);
                }
            }
            catch(IOException e) {
                throw new Error("Failed to create temporary file for " + nativeName + " library: " + e);
            }
            finally {
                try { is.close(); } catch(IOException e) { }
                if (fos != null) {
                    try { fos.close(); } catch(IOException e) { }
                }
            }
        }
        System.load(lib.getAbsolutePath());
	}
	
    private static String getNativeLibraryResourcePath(int osType, String arch, String name, String nativeResourcesRoot) {
        String osPrefix;
        arch = arch.toLowerCase();
        switch(osType) {
        case Platform.WINDOWS:
            if ("i386".equals(arch))
                arch = "x86";
            osPrefix = "win32-" + arch;
            break;
        case Platform.MAC:
            osPrefix = "darwin";
            break;
        case Platform.LINUX:
            if ("x86".equals(arch)) {
                arch = "i386";
            }
            else if ("x86_64".equals(arch)) {
                arch = "amd64";
            }
            osPrefix = "linux-" + arch;
            break;
        case Platform.SOLARIS:
            osPrefix = "sunos-" + arch;
            break;
        default:
            osPrefix = name.toLowerCase();
            if ("x86".equals(arch)) {
                arch = "i386";
            }
            if ("x86_64".equals(arch)) {
                arch = "amd64";
            }
            if ("powerpc".equals(arch)) {
                arch = "ppc";
            }
            int space = osPrefix.indexOf(" ");
            if (space != -1) {
                osPrefix = osPrefix.substring(0, space);
            }
            osPrefix += "-" + arch;
            break;
        }
        return nativeResourcesRoot + osPrefix;
    }
    
    private static class DeleteNativeLibrary extends Thread {
    	public DeleteNativeLibrary(File lib) {
    		lib.deleteOnExit();
    	}
    }
}
