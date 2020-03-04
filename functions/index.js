const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.senInvitationNotif = functions.firestore
    .document('users/{userId}')
    .onUpdate((change, context) => {

      // Get an object representing the document
      const newValue = change.after.data();
      console.log("after: " + newValue);

      // ...or the previous value before this update
//      const previousValue = change.before.data();
//      console.log("before: " + previousValue);

      // access a particular field as you would any JS property
      const invite = newValue.invitation.from;
      console.log("after: " + invite);

      const to_userId = context.params.userId;
      console.log("id: " + to_userId);

      const payload = {

      }

//      return admin.firestore().collection("users").doc(to_userId).
//              get().then(snapshot =>  {
//
//                 const from_userId =  snapshot.from
//
//             }).catch(reason =>  {
//                 // you should handle errors here
//             })

      // perform desired operations ...
    });

