package edu.hofstra.cs.csc17.Fakebook;
import java.util.Date;

public class UserPictureData implements Displayable{
    
    public User author;
    public Date dateTaken;
    public int fileSizeInMegaBytes;
    public String pictureDescription;

    public UserPictureData(User author, Date dateTaken, int fileSizeInMegaBytes, String pictureDescription){
        this.author = author;
        this.dateTaken = dateTaken;
        this.fileSizeInMegaBytes = fileSizeInMegaBytes;
        this.pictureDescription = pictureDescription;
    }

    public String getDisplay() {
        return(this.author.username + " has posted a " + this.pictureDescription + " on " + this.dateTaken + ".");
    }
}