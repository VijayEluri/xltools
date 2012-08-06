package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
	public static void serialize(Object obj, String filename){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(obj);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static Object deserialize(String filename){
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Object obj = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			obj = in.readObject();
			in.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}
		return obj;
	}
}
