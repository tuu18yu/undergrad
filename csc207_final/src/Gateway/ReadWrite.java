package Gateway;

import java.io.IOException;

/**
 * Interface providing methods for serializing and deserializing from files.
 *
 * @author Filip Jovanovic, Chaolin Wang
 */
public interface ReadWrite {
    // Reads a container from writeFilepath and stores it to the respective use case that has it
    Object readFromFile(String readFilepath) throws ClassNotFoundException, IOException;

    // Gets/accesses a container from a use case and serializes to the given file path
    void saveToFile(String writeFilepath, Object data) throws IOException;

}
