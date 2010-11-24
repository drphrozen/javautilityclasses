import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import dk.znz.serialization.SimpleSerializable;

public class NewsClip implements SimpleSerializable, Serializable {

	private static final long serialVersionUID = 2482783519399498054L;
	
	private String mTitle;
	private String mDescription;
	private String mImageLocation;
	private String mVideoLocation;
	private Date mPublished;
	private Date mUpdated;

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getImageLocation() {
		return mImageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.mImageLocation = imageLocation;
	}

	public String getVideoLocation() {
		return mVideoLocation;
	}

	public void setVideoLocation(String videoLocation) {
		this.mVideoLocation = videoLocation;
	}

	public Date getPublished() {
		return mPublished;
	}

	public void setPublished(Date published) {
		this.mPublished = published;
	}

	public Date getUpdated() {
		return mUpdated;
	}

	public void setUpdated(Date updated) {
		this.mUpdated = updated;
	}

	@Override
	public void serialize(DataOutputStream output) throws IOException {
		output.writeUTF(mTitle);
		output.writeUTF(mDescription);
		output.writeUTF(mImageLocation);
		output.writeUTF(mVideoLocation);
		output.writeLong(mPublished.getTime());
		output.writeLong(mUpdated.getTime());
	}

	@Override
	public void deserialize(DataInputStream input) throws IOException {
		mTitle = input.readUTF();
		mDescription = input.readUTF();
		mImageLocation = input.readUTF();
		mVideoLocation = input.readUTF();
		mPublished = new Date(input.readLong());
		mUpdated = new Date(input.readLong());
	}
}
