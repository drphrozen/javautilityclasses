import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;

import dk.znz.serialization.SimpleSerializable;
import dk.znz.serialization.SimpleSerializer;

public class Benchmark {

	public static void main(String[] args) throws IOException {
		writeObject(new SimpleObject());
		writeObject(new SimpleObject[] { new SimpleObject() });
		writeObject(new SimpleObject[] { new SimpleObject(), new SimpleObject() });
		writeObject(new SimpleObject[] { new SimpleObject(),
				new SimpleObject(), new SimpleObject() });
		writeObject(new SpecialObject());
		writeObject(new SpecialObject[] { new SpecialObject() });
		writeObject(new SpecialObject[] { new SpecialObject(),
				new SpecialObject() });
		writeObject(new SpecialObject[] { new SpecialObject(),
				new SpecialObject(), new SpecialObject() });
		writeObject(new int[] { 1 });
		writeObject(new int[] { 1, 2 });
		writeObject(new int[] { 1, 2, 3 });
		writeSimpleSerializable(new SerializableObject());
		writeSimpleSerializable(new SimpleObject());
		
		NewsClipStore store = new NewsClipStore();
		NewsClip[] clips = new NewsClip[10];
		for (int i = 0; i < clips.length; i++) {
			 NewsClip clip = new NewsClip();
			 clip.setTitle("TestTitle");
			 clip.setDescription("TestDescription");
			 clip.setImageLocation("http://test.com/image");
			 clip.setVideoLocation("http://test.com/video");
			 clip.setPublished(new Date());
			 clip.setUpdated(new Date(0L));
			 clips[i] = clip;
		}
		store.setClips(clips);
		long nanos = System.nanoTime();
		System.out.println("\nwriteSimpleSerializable");
		for (int i = 0; i < 100; i++)
			writeSimpleSerializable(store);
		update(nanos);
		nanos = System.nanoTime();
		System.out.println("\nwriteObject");
		for (int i = 0; i < 100; i++)
			writeObject(store);
		update(nanos);
		nanos = System.nanoTime();
		System.out.println("\nwriteCompressedSimpleSerializable");
		for (int i = 0; i < 100; i++)
			writeCompressedSimpleSerializable(store);
		update(nanos);
		nanos = System.nanoTime();
		System.out.println("\nwriteCompressedObject");
		for (int i = 0; i < 100; i++)
			writeCompressedObject(store);
		update(nanos);
	}
	
	private static void update(long startNanoTime) {
		long endTime = System.nanoTime();
		long diff = endTime - startNanoTime;
		double milliseconds = diff/1000000.0;
		System.out.println("Time: " + milliseconds + "ms");
	}

	private static void writeObject(Object o) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(byteArrayOutputStream);
		output.writeObject(o);
		output.flush();
		//System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
	}

	private static void writeCompressedObject(Object o) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
		ObjectOutputStream output = new ObjectOutputStream(deflaterOutputStream);
		output.writeObject(o);
		deflaterOutputStream.finish();
		output.flush();
		//System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
	}

	private static void writeSimpleSerializable(SimpleSerializable o) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		SimpleSerializer.serialize(byteArrayOutputStream, o);
		//System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
	}

	private static void writeCompressedSimpleSerializable(SimpleSerializable o) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DeflaterOutputStream deflateOutput = new DeflaterOutputStream(byteArrayOutputStream);
		SimpleSerializer.serialize(deflateOutput, o);
		deflateOutput.finish();
		//System.out.println(o.getClass().getSimpleName() + byteArrayOutputStream.toByteArray().length);
	}
}

class SimpleObject implements Serializable, SimpleSerializable {

	private static final long serialVersionUID = -3989239984559388644L;

	private int mIntenger; // 4
	private float mFloat; // 4
	private byte mByte; // 1

	public int getIntenger() {
		return mIntenger;
	}

	public void setIntenger(int intenger) {
		mIntenger = intenger;
	}

	public float getFloat() {
		return mFloat;
	}

	public void setFloat(float f) {
		mFloat = f;
	}

	public byte getByte() {
		return mByte;
	}

	public void setByte(byte b) {
		mByte = b;
	}

	@Override
	public void serialize(DataOutputStream output) throws IOException {
		output.writeInt(mIntenger);
		output.writeFloat(mFloat);
		output.writeByte(mByte);
	}

	@Override
	public void deserialize(DataInputStream input) throws IOException {
		mIntenger = input.readInt();
		mFloat = input.readFloat();
		mByte = input.readByte();
	}
}

class SpecialObject implements Serializable {

	private static final long serialVersionUID = 5675999350876668400L;

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		// out.write(0);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.read();
	}

}
