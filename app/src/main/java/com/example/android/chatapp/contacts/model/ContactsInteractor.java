package com.example.android.chatapp.contacts.model;

import com.example.android.chatapp.POJO.Chat;
import com.example.android.chatapp.POJO.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eslam on 14-Jan-18.
 */

public class ContactsInteractor {

    private final List<Chat> chats;
    private final List<String> chatsIDs;
    private final String currentUserID;
    // to store the friendsNames of the users
    private final List<User> friends;
    private final List<String> friendsIds;
    private final DatabaseReference databaseReference;
    private onFriendsReceived listener;

    public interface onFriendsReceived {
        void sendFriendsChats(List<Chat> chats, List<String> chatsIds, List<User> names);

        void thereIsNoData();
    }

    public ContactsInteractor() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        chats = new ArrayList<>();
        chatsIDs = new ArrayList<>();
        friends = new ArrayList<>();
        friendsIds = new ArrayList<>();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void fetchData(final onFriendsReceived listener) {
        chats.clear();
        friends.clear();
        friendsIds.clear();
        chatsIDs.clear();
        getChatsIDs();
        this.listener = listener;
    }


    //get the chats ids for the current user
    private void getChatsIDs() {
        databaseReference.child("userChats").child(currentUserID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                friends.clear();
                friendsIds.clear();
                chatsIDs.clear();
                if (dataSnapshot == null || !dataSnapshot.hasChildren()) {
                    listener.thereIsNoData();
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    chatsIDs.add(child.getKey());
                }
                getChats();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private final ValueEventListener chatValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            chats.add(dataSnapshot.getValue(Chat.class));

            if (chats.size() == chatsIDs.size())//get the friends ids after you reach the last chat
            {
                getFriendsIDs();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //get the chats by the ids
    private void getChats() {
        for (int i = 0; i < chatsIDs.size(); i++) {

            databaseReference.child("chats").child(chatsIDs.get(i)).addListenerForSingleValueEvent(chatValueEventListener);
        }

    }

    private void getFriendsIDs() {
        for (int i = 0; i < chats.size(); i++) {
            List<String> membersIDs = chats.get(i).getMembers();
            for (int j = 0; j < membersIDs.size(); j++) {
                if (!membersIDs.get(j).equals(currentUserID))//get the ids of the users except the current user
                {
                    friendsIds.add(membersIDs.get(j));
                }
            }
        }
        getfriendsInfo();
    }

    private final ValueEventListener userValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            friends.add(dataSnapshot.getValue(User.class));

            if (friends.size() == friendsIds.size()) {
                listener.sendFriendsChats(chats, chatsIDs, friends);
                return;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void getfriendsInfo() {
        for (int i = 0; i < chatsIDs.size(); i++) {

            databaseReference.child("users").child(friendsIds.get(i)).addListenerForSingleValueEvent(userValueEventListener);
        }
    }

}
