package info.mastera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VCardFile {

	private static final String CARDS_RECOGNISED = "%s cards recognised.";
	private static final String CARDS_SAVED = "%s cards saved.";
	private static final String ERROR_FILE_NOT_FOUND = "File \"%s\" not found. Wrong file name or argument in command line.";
	private static final String ERROR_IO_READ = "Error while reading information from file.";

	private static final String LINE_START = "BEGIN:VCARD";
	private static final String LINE_FINISH = "END:VCARD";

	private String folderName = "";

	private List<VCard> vCards = new ArrayList<>();

	private String getFolderName(File file) {
		return file.getName().replaceAll(".vcf", "");
	}

	private List<VCard> loadCards(List<String> lines) {
		List<VCard> result = new ArrayList<>();

		int i = 0;
		while (i < lines.size()) {
			if (lines.get(i).equals(LINE_START)) {
				List<String> vCard = new ArrayList<>();
				while ((i < lines.size()) && (!lines.get(i).equals(LINE_FINISH))) {
					vCard.add(lines.get(i));
					i++;
				}
				if (lines.get(i).equals(LINE_FINISH)) {
					vCard.add(lines.get(i));
					result.add(new VCard(vCard));
				}
			}
			i++;
		}
		System.out.println(String.format(CARDS_RECOGNISED, result.size()));
		return result;
	}

	private String getFileNumber(int currentNumber, int AllNumbers) {
		StringBuilder result = new StringBuilder();
		int countDigits = Integer.toString(AllNumbers).length();
		int currentCountDigits = Integer.toString(currentNumber).length();
		for (int i = 0; i < countDigits - currentCountDigits; i++) {
			result.append("0");
		}
		result.append(Integer.toString(currentNumber));
		return result.toString();
	}

	public void load(String fileName) {
		File file = new File(fileName);

		folderName = getFolderName(file);

		if (file.exists()) {
			List<String> lines = new ArrayList<>();
			try (FileReader fileReader = new FileReader(file)) {
				try (BufferedReader br = new BufferedReader(fileReader);) {
					String line = null;
					while ((line = br.readLine()) != null) {
						lines.add(line);
					}
				}
				if (lines.size() > 0) {
					setVCards(loadCards(lines));
				}
			} catch (FileNotFoundException e) {
				System.out.println(String.format(ERROR_FILE_NOT_FOUND, fileName));
			} catch (IOException e) {
				System.out.println(ERROR_IO_READ);
			}
		} else {
			try {
				System.out.println(String.format(ERROR_FILE_NOT_FOUND, file.getCanonicalFile()));
			} catch (IOException e) {
				System.out.println(ERROR_IO_READ);
			}
		}
	}

	public List<VCard> getVCards() {
		return vCards;
	}

	public void setVCards(List<VCard> vCards) {
		this.vCards = vCards;
	}

	public void save() {
		for (int i = 0; i < getVCards().size(); i++) {
			getVCards().get(i)
					.save("./" + folderName + "/vCardSplit" + getFileNumber(i + 1, getVCards().size()) + ".vcf");
		}
		System.out.println(String.format(CARDS_SAVED, getVCards().size()));
	}

}
