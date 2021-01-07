import java.util.List;
import java.util.ArrayList;
//import java.nio.file.Files;
import java.io.IOException;
//import java.util.InputMismatchException;
import java.nio.file.NoSuchFileException;
//import java.nio.file.FileSystem;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.util.Date;
import java.util.HashSet;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Set;


import edu.hofstra.cs.csc17.Fakebook.Displayable;
import edu.hofstra.cs.csc17.Fakebook.User;
import edu.hofstra.cs.csc17.Fakebook.UserPictureData;
import edu.hofstra.cs.csc17.Fakebook.ListOfUsers;

public class userPage{
    public static void main(String[] args) throws IOException{             
        try{            
            ListOfUsers listOfUsers = new ListOfUsers();
            String userFile = "people.txt";
            listOfUsers.addUsersFromFile(userFile);
            
            String relationshipsFile = "relationships.txt";
            addFriendsToUsers(relationshipsFile,listOfUsers);
            //listOfUsers.getDisplay();

            String fileNameOfPictureData= "content.txt";
            List<UserPictureData> listOfPictureData = assignPicturePostsToAUser(fileNameOfPictureData,listOfUsers);
            
            User loggedInUser = listOfUsers.userList.get(0);
            List<User> mainFeedFriends = findFriendsForMainFeed(loggedInUser,listOfUsers);
            List<User> recommendedFeedFriends = findFriendsOfFriendsForRecommendedFeed(loggedInUser,mainFeedFriends,listOfUsers);
            List<UserPictureData> mainFeed = createFeedData(mainFeedFriends, listOfPictureData);
            List<UserPictureData> recommendedFeed = createFeedData(recommendedFeedFriends, listOfPictureData);
            
            System.out.println(loggedInUser.username+"'s main feed: \n");
            mainFeed.forEach((post) -> System.out.println(post.getDisplay()));
            System.out.println("\n" + loggedInUser.username+ "'s recommended feed: \n");
            recommendedFeed.forEach((post) -> System.out.println(post.getDisplay()));
            
        }catch(NoSuchFileException exception){
            System.out.println("ERROR: The file " + exception.getFile() + " couldn't be read.");   
        }
    }

    public static void display(Displayable thingToDisplay){
        System.out.println(thingToDisplay.getDisplay()+ "\n");

    }
    public static void addFriendsToUsers(String relationshipFileName, ListOfUsers listOfUsers) throws IOException{
        List<String> linesOfRelationships = listOfUsers.turnFileIntoLines(relationshipFileName);   
        for(String lineBeingChecked: linesOfRelationships){
            String[] usernamesArray = lineBeingChecked.split("\\|"); 
            int numberOfLineWithError = 1 + linesOfRelationships.indexOf(lineBeingChecked);
            if(usernamesArray.length==2){                               
                if(listOfUsers.areUsersSeekingFriendshipReal(usernamesArray)){
                    listOfUsers.addFriend(usernamesArray);
                    
                }else{
                    System.out.println("\nERROR: When Adding a friend, at least one of the users on line " + Integer.toString(numberOfLineWithError) + " doesn't exist!");
                }
            }else{
                    
                System.out.println("\nERROR: There was an inappropriate amount of data provided to add a friend on line " + Integer.toString(numberOfLineWithError) + ":\n" + lineBeingChecked);
            }
        }
        return;
    }

    public static List<UserPictureData> assignPicturePostsToAUser(String fileNameOfPictureData, ListOfUsers listOfUsers) throws IOException{
        List<String> linesOfPicturesToPost = listOfUsers.turnFileIntoLines(fileNameOfPictureData);
        List<UserPictureData> listOfPictureData = new ArrayList<UserPictureData>();

        for(String lineOfPictureData: linesOfPicturesToPost){
            String[] arrayOfPictureData = lineOfPictureData.split("\\|");
            int numberOfLineWithError = 1 + linesOfPicturesToPost.indexOf(lineOfPictureData);
            try{
                if(listOfUsers.isUserPostingReal(arrayOfPictureData)&&(listOfUsers.instanceOfPictureData(arrayOfPictureData)!=null)){
                    UserPictureData newPictureData = listOfUsers.instanceOfPictureData(arrayOfPictureData);
                    listOfPictureData.add(newPictureData);
                }
                else if(!listOfUsers.isUserPostingReal(arrayOfPictureData)){
                    String falseUsername = arrayOfPictureData[0];
                    System.out.println("\nERROR: The user " + falseUsername +  " from which picture data supposedly comes from on line " + Integer.toString(numberOfLineWithError) + " does not exist!");
                }else if(arrayOfPictureData.length!=4){
                    System.out.println("\nERROR: There was an inappropriate amount of data to record the picture data on line " + Integer.toString(numberOfLineWithError) + "\n" + lineOfPictureData + "\n");
                }else{
                    String sizeOfPicture = arrayOfPictureData[2];
                    System.out.println("\nERROR: There was an innapropriate file size of " + sizeOfPicture + " on line " + Integer.toString(numberOfLineWithError) + ".");
                }
            }catch(NumberFormatException exception){
                System.out.println("\nERROR: " + exception.getMessage() + " is not a number!");
            }
        }
        return listOfPictureData;
    }

    public static List<User> findFriendsForMainFeed(User loggedInUser,  ListOfUsers listOfUsers){
        if(listOfUsers==null){
            System.out.println("The given list was empty when calling findFriendsForMainFeed.");
            List<User> nullList = null;
            return nullList;
        }
        if(!listOfUsers.isUserReal(loggedInUser)){
            System.out.println(loggedInUser + " cannot get a feed becuase the username has not been created!");
            List<User> nullList = null;
            return nullList;
        }else{
            List<User> loggedInUsersFriends = new ArrayList<User>();
            for(User friendOfUser:loggedInUser.friends){
                loggedInUsersFriends.add(friendOfUser);
            }
            return loggedInUsersFriends;
        }
    }

    public static List<User> findFriendsOfFriendsForRecommendedFeed(User loggedInUser, List<User> loggedInUsersFriends, ListOfUsers listOfUsers){
        if (loggedInUsersFriends==null ||listOfUsers.userList==null){
            System.out.println("One of the given lists were empty when calling findFriendsOfFriendsForRecommendedFeed.");
            List<User> nullList = null;
            return nullList;
        }
        Set<User> friendsOfFriendsSet = new HashSet<User>();
        friendsOfFriendsSet.addAll(listOfUsers.findFriendsofFriends(loggedInUsersFriends));
        Set<User> loggedInUsersFriendsSet = new HashSet<User>();
        for(User friend:loggedInUsersFriends){
            loggedInUsersFriendsSet.add(friend);
        }
        friendsOfFriendsSet.remove(loggedInUser); 
        friendsOfFriendsSet.removeAll(loggedInUsersFriendsSet);
        List<User> finalizedFriendsOfFriendsList= new ArrayList<User>();
        for(User aUser: friendsOfFriendsSet){
            finalizedFriendsOfFriendsList.add(aUser);
        }
        return finalizedFriendsOfFriendsList;
    }

    public static List<UserPictureData> createFeedData(List<User> usersForSomeFeed, List<UserPictureData> listOfPictureData){
        if(usersForSomeFeed==null||listOfPictureData==null){
            System.out.println("One of the given lists were empty when calling createFeedData.");
            List<UserPictureData> nullList = null;
            return nullList;
        }
        List<UserPictureData> dataForFeed = new ArrayList<UserPictureData>();
        for(UserPictureData dataOfOnePicturePost: listOfPictureData){
            for(User userToPutInFeed:usersForSomeFeed){
                if(dataOfOnePicturePost.author.username.equals(userToPutInFeed.username)&&dataForFeed.size()<10){
                    dataForFeed.add(dataOfOnePicturePost);
                }
            }
        }
        return dataForFeed;
    }
}