package info.mastera;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class VCard {

	private static final String ERROR_IO_WRITE = "Error while saving vCard to \"%s\" .";
	private static final String ERROR_FILE_FOLDER = "Can't create folder. There is file \"%s\" on this path.";
	private static final String FOLDER_CREATED = "Folder \"%s\" created.";
	private List<String> lines;

	public VCard() {
		this.lines = new ArrayList<>();
	}

	private void checkPath(String fileName) throws IOException {
		File file = new File(fileName);
		String dir = file.getCanonicalPath().replaceAll(file.getName(), "");

		File fileDir = new File(dir);
		if ((fileDir.exists() && fileDir.isFile())) {
			System.out.println(String.format(ERROR_FILE_FOLDER, fileDir));
		} else if (!fileDir.exists()) {
			fileDir.mkdir();
			System.out.println(String.format(FOLDER_CREATED, dir));
		}
	}

	public VCard(List<String> lines) {
		this.lines = lines;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public void saveFiles(String fileName) {
		try {
			checkPath(fileName);
			Path filePath = Paths.get(fileName);
			if (!Files.exists(filePath)) {
				Files.createFile(filePath);
			}
			Files.write(filePath, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.out.println(String.format(ERROR_IO_WRITE, fileName));
		}
	}

	public void save(String fileName) {
		File file = new File(fileName);
		try {
			checkPath(fileName);
			try (FileWriter fileWriter = new FileWriter(file)) {
				try (BufferedWriter br = new BufferedWriter(fileWriter);) {
					for (String line : lines) {
						br.write(line);
						br.newLine();
					}
				}
			}
		} catch (IOException e1) {
			System.out.println(String.format(ERROR_IO_WRITE, fileName));
		}
	}

}
