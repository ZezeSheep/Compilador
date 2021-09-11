package AnalisadorSintatico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GeradorCodigo {
	
	private String nomeArquivo;
	private FileOutputStream  fileStream;
    private OutputStreamWriter streamWriter;
    
	public GeradorCodigo(String nomeArquivo){
		this.nomeArquivo = nomeArquivo;
		try {
			fileStream = new FileOutputStream(this.nomeArquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		streamWriter = new OutputStreamWriter(fileStream);
	}
	
	public void cancelarArquivo() {
		concluirEscrita();
		File file = new File(nomeArquivo); 
		file.delete();
	}
	
	public void concluirEscrita() {
		try {
			streamWriter.close();
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void escreverStringEmArquivo(String comando) {
		try {
			streamWriter.write(comando+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
