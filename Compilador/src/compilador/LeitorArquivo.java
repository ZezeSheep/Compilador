/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael Assis
 */
public class LeitorArquivo {
    
    private String _arquivo;
    private FileInputStream  _fileStream;
    private InputStreamReader _streamReader;

    public String getArquivo() {
        return _arquivo;
    }

    public void setArquivo(String _arquivo) {
        this._arquivo = _arquivo;      
    }

    public LeitorArquivo(String arquivo){
        this._arquivo = arquivo;        
        try {
			_fileStream = new FileInputStream(this._arquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        _streamReader = new InputStreamReader(_fileStream);
    }
    
    public char getChar(){
        try {
            return (char) _streamReader.read();
        } catch (IOException ex) {
            Logger.getLogger(LeitorArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }


}
