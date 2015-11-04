package com.zlk.bigdemo.freeza.widget.selector.support;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem implements Parcelable{

	private long imageId;
	private String imagePath;
	private int isSelect;
	
	public ImageItem(){}
	
    public ImageItem(long id,String imagePath) {
    	this.imageId = id;
        this.imagePath = imagePath;
    }

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int isSelect() {
		return isSelect;
	}

	public void setSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(imageId);
		out.writeString(imagePath);
		out.writeInt(isSelect);
	}

	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		public ImageItem createFromParcel(Parcel in) {
			return new ImageItem(in);
		}

		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};

	private ImageItem(Parcel in) {
		imageId = in.readLong();
		imagePath = in.readString();
		isSelect = in.readInt();
	}
}
