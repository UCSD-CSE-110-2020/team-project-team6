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

      if(newValue.invitation.toOrFrom === "from"){
          const payload = {
                notification:{
                    title: inviteFrom,
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
            return "the field invitation hasn't changed !";
      }

    });

    exports.sendTeamNotifications = functions.firestore
       .document('team_notifications/{teamId}/messages/{messageId}')
       .onCreate((snap, context) => {
         // Get an object with the current document value.
         // If the document does not exist, it has been deleted.
         const document = snap.exists ? snap.data() : null;

          console.log('sending team message... ' + snap.data());

         if (document) {
           var message = {
             notification: {
               title: document.fromEmail,
               body: document.messages
             },
             topic: context.params.teamId
           };

           return admin.messaging().send(message)
             .then((response) => {
               // Response is a message ID string.
               console.log('Successfully sent message:', response);
               return response;
             })
             .catch((error) => {
               console.log('Error sending message:', error);
               return error;
             });
         }

         return "document was null or emtpy";
       });