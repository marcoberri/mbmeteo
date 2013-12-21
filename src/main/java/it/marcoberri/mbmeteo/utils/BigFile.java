/*
 * 
 *  Classe per la lettura di file di grossa mole
 *  grazie a http://code.hammerpig.com/how-to-read-really-large-files-in-java.html
 */

package it.marcoberri.mbmeteo.utils;

import java.io.*;
import java.util.Iterator;

/**
 *
 * @author marco
 */
public class BigFile implements Iterable<String>
{
    private BufferedReader _reader;

    /**
     *
     * @param filePath
     * @throws Exception
     */
    public BigFile(String filePath) throws Exception
    {
	_reader = new BufferedReader(new FileReader(filePath));
    }

    /**
     *
     */
    public void Close()
    {
	try
	{
	    _reader.close();
	}
	catch (Exception ex) {}
    }

    /**
     *
     * @return
     */
    public Iterator<String> iterator()
    {
	return new FileIterator();
    }

    private class FileIterator implements Iterator<String>
    {
	private String _currentLine;

	public boolean hasNext()
	{
	    try
	    {
		_currentLine = _reader.readLine();
	    }
	    catch (Exception ex)
	    {
		_currentLine = null;
		ex.printStackTrace();
	    }

	    return _currentLine != null;
	}

	public String next()
	{
	    return _currentLine;
	}

	public void remove()
	{
	}
    }
}
