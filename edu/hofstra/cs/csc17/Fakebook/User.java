package edu.hofstra.cs.csc17.Fakebook;

import java.util.List;
import java.util.ArrayList;

public class User implements Displayable{
    public String username;
    public List<User> friends; 
    public User friendObject;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;

    public User(String username,String firstName, String lastName, int age, String gender){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.friends = new ArrayList<User>();
    }

    public void getName(){
        System.out.println(firstName + " " + lastName);
    }

    public void getUsername(){
        System.out.println("This person's username is: " + username);
    }

    public void userData(){
        System.out.println("Personal data:\n"+  "Age: " + age +  "\nGender:" + gender + "\n");

    }

    public void addFriend(User theFriend){
        this.friends.add(theFriend); 
        //this.friendObject = theFriend;
    }

    public String getDisplay(){
        if (this.friends.size() == 1 ){
            /*if (this.friendObject.usersFriend != null){
                return ("Username: " + this.username + "\nUser's friend: " + this.usersFriend + "\nThat users friend: " + this.friendObject.usersFriend );
            }*/
            return ("Username: " + this.username + "\nUser's friend: " + this.friends.get(0).username);
        }
        else if(this.friends.size() > 1){
            String listOfFriends="";
            for(User friend:this.friends){
                listOfFriends= listOfFriends + " " + friend.username;
            }
            return ("Username: " + this.username + "\nUser's friends: " + "[" + listOfFriends+ "]");
        }
        else{
            return("Username: " + this.username);
        }
    }
    
}
