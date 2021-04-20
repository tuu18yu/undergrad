package Gateway;

import java.io.*;

/**
 * Provides methods for serializing and deserializing data by saving Objects to
 * file and reading Objects from files.
 *
 * @author Filip Jovanovic, Chaolin Wang
 */
public class ReadWriteGateway implements ReadWrite {

    /**
     * Deserializes data from a file into an Object.
     * Throws ClassNotFoundException if class of Object read in does not exist.
     *
     * @param filepath The filepath to the file to read from.
     * @return Object containing deserialized data, or null if the file could not be read.
     * @throws ClassNotFoundException - If class of read Object does not exist.
     * @throws IOException - If an I/O error occurs while opening the file.
     */
    @Override
    public Object readFromFile(String filepath) throws ClassNotFoundException, IOException {
        InputStream file = new FileInputStream(filepath);
        ObjectInput input = new ObjectInputStream(file);
        return input.readObject();
    }

    /**
     * Serializes an Object into a file.
     * Throws IOException
     *
     * @param filepath The file path to write to
     * @param data The object to serialize to the file
     * @throws IOException - If an I/O error occurs while opening the file.
     */
    @Override
    public void saveToFile(String filepath, Object data) throws IOException {
        OutputStream file = new FileOutputStream(filepath);
        ObjectOutput output = new ObjectOutputStream(file);

        output.writeObject(data);

        output.close();
        file.close();
    }


}
