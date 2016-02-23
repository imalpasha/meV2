package com.fly.firefly.drawer;

import android.os.Parcel;
import android.os.Parcelable;

public class DrawerItem implements Parcelable
{

	private int id;
	private Object tag;
	private String title;
	private int iconId;
	private int layoutId;
	private int backgroundColor;
	private int lvl;
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getLvl()
	{
		return lvl;
	}

	public void setLvl(int lvl)
	{
		this.lvl = lvl;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Object getTag()
	{
		return tag;
	}

	public void setTag(Object tag)
	{
		this.tag = tag;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getIconId()
	{
		return iconId;
	}

	public void setIconId(int iconId)
	{
		this.iconId = iconId;
	}

	public int getLayoutRes()
	{
		return layoutId;
	}

	public void setLayoutId(int layoutId)
	{
		this.layoutId = layoutId;
	}

	public int getLayoutId()
	{
		return layoutId;
	}

	public int getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeInt(iconId);
		dest.writeInt(layoutId);
		dest.writeInt(backgroundColor);
		// dest.writeByte((byte) (checked ? 1 : 0));
	}

	public static final Creator<DrawerItem> CREATOR = new Creator<DrawerItem>()
	{
		public DrawerItem createFromParcel(Parcel in)
		{
			return new DrawerItem(in);
		}

		public DrawerItem[] newArray(int size)
		{
			return new DrawerItem[size];
		}
	};

	private DrawerItem(Parcel in)
	{
		id = in.readInt();
		title = in.readString();
		iconId = in.readInt();
		layoutId = in.readInt();
		backgroundColor = in.readInt();
		// checked = in.readByte() != 0;
	}

	public DrawerItem()
	{
	}
}
