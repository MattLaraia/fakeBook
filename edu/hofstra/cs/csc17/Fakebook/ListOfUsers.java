package edu.hofstra.cs.csc17.Fakebook;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.io.IOException;
//import java.util.InputMismatchException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
//import java.nio.file.Path;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Set;
import java.util.HashSet;

public class ListOfUsers{
    public List<User> userList;

    public ListOfUsers(){

    }

    public void getDisplay(){
        this.userList.forEach((user) -> System.out.println(user.username));
        return;
    }

    public void addUsersFromFile(String fileName) throws IOException{
        List<String> linesOfUsers = turnFileIntoLines(fileName);
        List<User> listOfUsers = new ArrayList<User>();

        for(String lineBeingChecked : linesOfUsers){  
            try{
                String[] wordsArray = lineBeingChecked.split("\\|");
                String userAgeString = wordsArray[3];
                int userAge = Integer.parseInt(userAgeString);
                if(wordsArray.length==5 && userAge>0 && userAge<120){
                    String username = wordsArray[0];
                    String firstName = wordsArray[1];
                    String lastName = wordsArray[2];
                    String gender = wordsArray[4];
                    User newUser = new User(username,firstName,lastName,userAge,gender);
                    listOfUsers.add(newUser);
                    
                }
                else if(userAge<=0 || userAge>=120){
                    System.out.println("\nERROR: The user was given an unrealistic age");
                }
                else {
                    int numberOfLineWithError = 1 + linesOfUsers.indexOf(lineBeingChecked);
                    System.out.println("\nERROR: There was an inappropraite amount of data to create a user on line" +  Integer.toString(numberOfLineWithError) +    ":\n" + lineBeingChecked + "\n");
                }
            }catch(NumberFormatException exception){
                System.out.println("\nERROR: " + exception.getMessage() + " is not an age!");
            }
            
        }
        this.userList=listOfUsers;
        return;
    }
    
    public List<String> turnFileIntoLines(String fileName) throws IOException{
        FileSystem defaultFS = FileSystems.getDefault(); 
        Path pathToFile = defaultFS.getPath(fileName);
        List<String> linesOfFile = Files.readAllLines(pathToFile);
        return linesOfFile;
    }

    public boolean areUsersSeekingFriendshipReal(String[] relationshipUsernames){
        boolean userToAddFriend = false;
        boolean friendBeingAdded = false;
        String usernameOfReceivingFriend = relationshipUsernames[0];
        String theFriendsUsername = relationshipUsernames[1];
        for(User aUser:this.userList){
            if(usernameOfReceivingFriend.equals(aUser.username)){
                userToAddFriend=true;
            }
            if(theFriendsUsername.equals(aUser.username)){
                friendBeingAdded=true;
            }
        }
        if(userToAddFriend&&friendBeingAdded){
            return true;
        }
        return false;
    }
    public boolean isUserPostingReal(String[] arrayOfPictureData) throws IOException{
        try{
            //named arrayOfPictureData elements for clarity
            String dateOfPost = arrayOfPictureData[1];
            String sizeOfPictureInString = arrayOfPictureData[2];
            int sizeOfPicture = Integer.parseInt(sizeOfPictureInString);
            Date DateOfPicturePosted = new SimpleDateFormat("dd/MM/yyyy").parse(dateOfPost);
            String pictureDescription = arrayOfPictureData[3];  
            if(arrayOfPictureData.length==4 ){  
                for(User aUser:this.userList){
                    String usernameThatPosted = arrayOfPictureData[0];
                    if(usernameThatPosted.equals(aUser.username)){
                        return true;
                    }
                }
            }
            
        }catch(ParseException exception){
            System.out.println("\nERROR: " + exception.getMessage() + " is not a date!");   
        }
        return false;
    }
    public void addFriend(String[] relationshipUsernames){
        String usernameOfReceivingFriend = relationshipUsernames[0];
        String theFriendsUsername = relationshipUsernames[1];
        for(User aUser:this.userList){
            if(usernameOfReceivingFriend.equals(aUser.username)){
                for(User aUsersFriend:this.userList){
                    if(theFriendsUsername.equals(aUsersFriend.username)){
                        aUser.addFriend(aUsersFriend);
                    }
                }
            }
        }
    }

    public UserPictureData instanceOfPictureData(String[] arrayOfPictureData) throws IOException{
        try{
            String sizeOfPictureInString = arrayOfPictureData[2];
            int sizeOfPicture = Integer.parseInt(sizeOfPictureInString);
            String dateOfPost = arrayOfPictureData[1];
            Date DateOfPicturePosted = new SimpleDateFormat("dd/MM/yyyy").parse(dateOfPost);
            String pictureDescription = arrayOfPictureData[3]; 
            if(sizeOfPicture>0){
                for(User aUser:this.userList){
                    String usernameThatPosted = arrayOfPictureData[0];
                    if(usernameThatPosted.equals(aUser.username)){
                        UserPictureData newPictureData = new UserPictureData(aUser,DateOfPicturePosted,sizeOfPicture,pictureDescription);
                        return newPictureData;
                    }
                }
            }
        }catch(ParseException exception){
            System.out.println("\nERROR: " + exception.getMessage() + " is not a date!");   
        }
        return null;
    }

    public boolean isUserReal(User someUser){
        for(User aUser: this.userList){
            if(someUser.equals(aUser)){
                return true;
            }
        }
        return false;
    }


    public Set<User> findFriendsofFriends(List<User> loggedInUsersFriends){
        Set<User> friendsofFriendsSet = new HashSet<User>();
        for(User aFriend:loggedInUsersFriends){
            for(User aUser:this.userList){
                if(aFriend.username.equals(aUser.username)){
                    for(User theirFriend: aFriend.friends){
                        friendsofFriendsSet.add(theirFriend);
                    }
                }
            }
        }
        return friendsofFriendsSet;
    }
}
