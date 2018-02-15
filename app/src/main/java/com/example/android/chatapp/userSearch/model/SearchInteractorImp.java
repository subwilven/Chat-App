package com.example.android.chatapp.userSearch.model;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eslam on 12-Jan-18.
 */

public class SearchInteractorImp {
    private final List<String> friendsIDs;
    private final DatabaseReference databaseReference;
    private final String currentUserID;
    private CallBack listener;
    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!dataSnapshot.getKey().equals(currentUserID))
                listener.sendResult(dataSnapshot.getValue(User.class), dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot == null || !dataSnapshot.hasChildren())
                listener.onDataChanged(false);
            else
                listener.onDataChanged(true);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public SearchInteractorImp() {
        friendsIDs = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //when intialize the objectnk
        getUserFriends(currentUserID);
    }

    public interface CallBack {
        void sendResult(User user, String userId);

        void onDataChanged(boolean hasData);
    }

    public void search(String searchText, CallBack listener) {
        this.listener = listener;
        databaseReference.removeEventListener(childEventListener);
        if (!searchText.isEmpty()) {
            databaseReference.child("users")
                    .orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff").addChildEventListener(childEventListener);
            databaseReference.child("users")
                    .orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff").addValueEventListener(valueEventListener);
        } else {
            databaseReference.child("users").orderByChild("name").addChildEventListener(childEventListener);
            databaseReference.child("users").orderByChild("name").addValueEventListener(valueEventListener);
        }

    }

    private void getUserFriends(String currentUserID) {
        databaseReference.child("userFriends").child(currentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friendsIDs.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFriend(String friendId) {
        String newChatKey = databaseReference.child("userChats").child(currentUserID).push().getKey();
        //add the chat to the current user chats
        databaseReference.child("userChats").child(currentUserID).child(newChatKey).setValue(true);
        // add that user_one as friend to the current user_two
        databaseReference.child("userFriends").child(currentUserID).child(friendId).setValue(true);
        // add that user_two as friend to the current user_one
        databaseReference.child("userFriends").child(friendId).child(currentUserID).setValue(true);

        //create a new chat
        // the members of the new chat
        List<String> members = new ArrayList<>();
        members.add(currentUserID);
        members.add(friendId);
        databaseReference.child("chats").child(newChatKey).setValue(new Chat(members));
    }

    public boolean isFriend(String id) {
        for (int i = 0; i < friendsIDs.size(); i++) {
            if (friendsIDs.get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }
}
