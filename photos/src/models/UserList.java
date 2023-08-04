package models;

import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class UserList implements Serializable {
    private List<User> userList = new ArrayList<User>();

    public final String storeFile = "src/attributes/users.ser";
    static final long serialVersionUID = 1L;

    private void initialize() {
        try {
            FileInputStream fileIn = new FileInputStream(storeFile);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            userList = (ArrayList<User>) ois.readObject();
            ois.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            userList = new ArrayList<>();
        }
    }

    /**
     * @throws IOException
     */
    public void saveData() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
        oos.writeObject(userList);
        oos.close();
    }

    
    /** 
     * @param user
     * @throws IOException
     */
    public void addUser(User user) throws IOException {
        initialize();

        if (user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        for (User u : userList) {
            if (u.getUsername().equals(user.getUsername())) {
                throw new IllegalArgumentException("Username already taken.");
            }
        }
        userList.add(user);
        saveData();
    }

    
    /** 
     * @param id
     * @return User
     */
    public User getUser(String id) {
        initialize();
        List<User> filtered = userList.stream().filter(u -> u.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("User does not exist.");
        }
        return filtered.get(0);
    }

    
    /** 
     * @param username
     * @return User
     */
    public User getUserByUsername(String username) {
        initialize();
        List<User> filtered = userList.stream().filter(u -> u.getUsername().equals(username))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("User does not exist.");
        }
        return filtered.get(0);
    }

    
    /** 
     * @param id
     * @param username
     * @param password
     * @throws IOException
     */
    public void editUser(String id, String username, String password) throws IOException {
        initialize();
        List<User> filtered = userList.stream().filter(u -> u.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("User does not exist.");
        }

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                throw new IllegalArgumentException("Username already taken.");
            }
        }

        User user = filtered.get(0);
        user.setUsername(username);
        user.setPassword(password);
        saveData();
    }

    public void deleteUser(String id) throws IOException {
        initialize();
        List<User> filtered = userList.stream().filter(a -> a.getID().equals(id)).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            throw new NoSuchElementException("The user to delete does not exist.");
        }
        userList = userList.stream().filter(p -> !p.getID().equals(id)).collect(Collectors.toList());
        saveData();
    }

    public List<User> getUsers() {
        initialize();
        return userList;
    }

    public void printUserList() {
        initialize();
        for (User u : userList) {
            System.out.println(u.toString());
        }
    }

    public boolean checkUsername(String username) {
        initialize();
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public void setUpUsers() throws IOException {
        initialize();

        if (checkUsername("stock")) {
            // Stock user already exists, no need to create it again.
            return;
        }

        User stock = new User("stock", "stock");
        Album album = new Album("stock", stock.getID());

        File storage = new File("data");
        FileFilter ff = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.matches(".*\\.(jpg|jpeg|png|gif)");
            }
        };

        File[] stockPhotos = storage.listFiles(ff);

        for (File file : stockPhotos) {
            album.addPhoto(new Photo(album.getID(), file));
        }

        stock.addAlbum(album);
        addUser(stock);
    }

}