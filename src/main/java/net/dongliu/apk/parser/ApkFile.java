package net.dongliu.apk.parser;

import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.utils.Inputs;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * ApkFile, for parsing apk file info.
 * This class is not thread-safe.
 *
 * @author dongliu
 */
public class ApkFile extends AbstractApkFile implements Closeable {

    private final ZipFile zf;
    private File apkFile;

    public ApkFile(File apkFile) throws IOException {
        this.apkFile = apkFile;
        // create zip file cost time, use one zip file for apk parser life cycle
        this.zf = new ZipFile(apkFile);
    }

    public ApkFile(String filePath) throws IOException {
        this(new File(filePath));
    }

    @Override
    protected List<CertificateFile> getAllCertificateData() throws IOException {
        Enumeration<? extends ZipEntry> enu = zf.entries();
        List<CertificateFile> list = new ArrayList<>();
        while (enu.hasMoreElements()) {
            ZipEntry ne = enu.nextElement();
            if (ne.isDirectory()) {
                continue;
            }
            String name = ne.getName().toUpperCase();
            if (name.endsWith(".RSA") || name.endsWith(".DSA")) {
                list.add(new CertificateFile(name, Inputs.readAll(zf.getInputStream(ne))));
            }
        }
        return list;
    }

    @Override
    public byte[] getFileData(String path) throws IOException {
        ZipEntry entry = zf.getEntry(path);
        if (entry == null) {
            return null;
        }

        InputStream inputStream = zf.getInputStream(entry);
        return Inputs.readAll(inputStream);
    }

    @Override
    protected ByteBuffer fileData() throws IOException {
        FileChannel channel = new FileInputStream(apkFile).getChannel();
        return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
    }


    @Override
    public ApkSignStatus verifyApk() throws IOException {
        ZipEntry entry = zf.getEntry("META-INF/MANIFEST.MF");
        if (entry == null) {
            // apk is not signed;
            return ApkSignStatus.notSigned;
        }

        try (JarFile jarFile = new JarFile(this.apkFile)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            byte[] buffer = new byte[8192];

            while (entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if (e.isDirectory()) {
                    continue;
                }
                try (InputStream in = jarFile.getInputStream(e)) {
                    // Read in each jar entry. A security exception will be thrown if a signature/digest check fails.
                    int count;
                    while ((count = in.read(buffer, 0, buffer.length)) != -1) {
                        // Don't care
                    }
                } catch (SecurityException se) {
                    return ApkSignStatus.incorrect;
                }
            }
        }
        return ApkSignStatus.signed;
    }

    @Override
    public void close() throws IOException {
        super.close();
        zf.close();
    }

}
