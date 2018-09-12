package info.mastera;

public class VCardExecuter {

	public static void main(String[] args) {
		String fileName = "./contacts.vcf";

		if (args.length > 0) {
			fileName = args[0];
		}

		VCardFile vCardFile = new VCardFile();
		vCardFile.load(fileName);

		vCardFile.save();
	}

}
