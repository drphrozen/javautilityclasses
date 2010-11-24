import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import dk.znz.serialization.SimpleSerializable;


public class NewsClipStore implements SimpleSerializable, Serializable {

	private static final long serialVersionUID = 7042704507678009466L;

	private NewsClip[] mClips;
	
	public NewsClip[] getClips() {
		return mClips;
	}

	public void setClips(NewsClip[] clips) {
		this.mClips = clips;
	}

	@Override
	public void serialize(DataOutputStream output) throws IOException {
		output.writeInt(mClips.length);
		for (int i = 0; i < mClips.length; i++) {
			mClips[i].serialize(output);
		}
	}

	@Override
	public void deserialize(DataInputStream input) throws IOException {
		int length = input.readInt();
		mClips = new NewsClip[length];
		for (int i = 0; i < length; i++) {
			NewsClip clip = new NewsClip();
			clip.deserialize(input);
			mClips[i] = clip;
		}
	}
}