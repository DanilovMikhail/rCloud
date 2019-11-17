package net.danilovms.rcloud.common;

import java.io.*;
import java.nio.channels.*;
import javax.xml.bind.DatatypeConverter;

public class EncodeDecode {
    public static void main(String[] args) throws Exception {
//        File file = new File("/bin/ls");
//        byte[] bytes = loadFile(file, new ByteArrayOutputStream()).toByteArray();
//        String encoded = DatatypeConverter.printBase64Binary(bytes);
//        System.out.println(encoded);
//        byte[] decoded = DatatypeConverter.parseBase64Binary(encoded);
//        // check
//        for (int i = 0; i < bytes.length; i++) {
//            assert bytes[i] == decoded[i];
//        }
    }

//    private static <T extends OutputStream> T loadFile(File file, T out)
//            throws IOException {
//        FileChannel in = new FileInputStream(file).getChannel();
//        try {
//            assert in.size() == in.transferTo(0, in.size(), Channels.newChannel(out));
//            return out;
//        } finally {
//            in.close();
//        }
//    }
}
