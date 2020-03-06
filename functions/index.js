const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.senInvitationNotif = functions.firestore
    .document('users/{userId}')
    .onUpdate((change, context) => {

      // Get an object representing the document
      const newValue = change.after.data();
      // ...or the previous value before this update
      const previousValue = change.before.data();
      //console.log("before: " + previousValue);


      // access a particular field as you would any JS property
      const inviteFrom = newValue.invitation.email;
      console.log("Email from: " + inviteFrom);

      const to_userId = context.params.userId;
      console.log("id: " + to_userId);

      const token_id = newValue.token_id;
      console.log("token ID: " + token_id);

      if(newValue.invitation !== previousValue.invitation){
          const payload = {
                notification:{
                    title: "Notification from: " + inviteFrom,
                    body: newValue.invitation.message,
                    icon: "default"
                }
          }

          return admin.messaging().sendToDevice(token_id, payload).then(response => {
               console.log('Successfully sent message:', response);
               return response;
          }).catch((error) => {
                console.log('Error sending message:', error);
                return error;
          });
      }else{
            console.log("the field invitation hasn't changed !");
      }

    });

