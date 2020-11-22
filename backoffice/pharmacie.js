var config = {
    apiKey: "AIzaSyCSMTlPBU1vUCbGSbbS6X9MCkT0YzV5H0M",
    authDomain: "taxitracking-fbae3.firebaseapp.com",
    databaseURL: "https://taxitracking-fbae3.firebaseio.com",
    projectId: "taxitracking-fbae3",
    storageBucket: "taxitracking-fbae3.appspot.com",
    messagingSenderId: "780931565513"
};

firebase.initializeApp(config);
var db = firebase.database();

// CREATE REWIEW
/*
var reviewForm = document.getElementById('reviewForm');

var fullName = document.getElementById('fullName');
var message = document.getElementById('message');
var hiddenId = document.getElementById('hiddenId');
*/
var marker;

function initMap() {
    var uluru = {
        lat: 35.765255413654984,
        lng: 10.82071868607136
    };
    var map = new google.maps.Map(document.getElementById('gmap_basic_example'), {
        zoom: 15,
        center: uluru
    });
    /*var marker = new google.maps.Marker({
        position: uluru,
        map: map
    });*/

    google.maps.event.addListener(map, "click", function(e) {
        if (marker && marker.setMap) {
            marker.setMap(null);
        }
        //lat and lng is available in e object
        var latLng = e.latLng;
        lat.value = latLng.lat();
        lng.value = latLng.lng();
        var uluru1 = {
            lat: latLng.lat(),
            lng: latLng.lng()
        };
        marker = new google.maps.Marker({
            position: uluru1,
            map: map
        });

    });


}

var reviewForm = document.getElementById('reviewForm');

var nom = document.getElementById('nom');
var tel = document.getElementById('tel');
var adresse = document.getElementById('adresse');
var lat = document.getElementById('lat');
var lng = document.getElementById('lng');
//type: $('input[name=group22]:checked', '#reviewForm').val(),
//var nuitjour = document.querySelector('input[name="group22"]:checked');
//var nuitjour = $('input[name=group22]:checked', '#reviewForm');
var nuitjour = document.getElementById("gp");
var hiddenId = document.getElementById('hiddenId');

reviewForm.addEventListener('submit', (e) => {
    e.preventDefault();

    if (!nom.value || !tel.value || !adresse.value || !lat.value || !lng.value || !nuitjour.value) return null

    var id = hiddenId.value || Date.now()

    db.ref('ehealth/pharmacie/' + id).set({
        nom: nom.value,
        tel: tel.value,
        adresse: adresse.value,
        lat: lat.value,
        lng: lng.value,
        nuitjour: nuitjour.value,


    });



    nom.value = '';
    tel.value = '';
    adresse.value = '';
    lat.value = '';
    lng.value = '';
    nuitjour.value = '';

});

// READ REVEIWS

var reviews = document.getElementById('reviews');
var reviewsRef = db.ref('ehealth/pharmacie');
var numberuser = document.getElementById('numberuser');
var numberusers = document.getElementById('users');

var ref = db.ref('ehealth');
ref.child("User").on("value", function(snapshot) {
    //console.log("There are " + snapshot.numChildren() + " messages");
    //alert(snapshot.numChildren());
    numberuser.innerHTML = snapshot.numChildren();
    numberusers.innerHTML = snapshot.numChildren();
});


reviewsRef.on('child_added', (data) => {
    var tr = document.createElement('tr')
    tr.id = data.key;
    tr.innerHTML = reviewTemplate(data.val())
    reviews.appendChild(tr);
});



reviewsRef.on('child_changed', (data) => {
    var reviewNode = document.getElementById(data.key);
    reviewNode.innerHTML = reviewTemplate(data.val());
});

reviewsRef.on('child_removed', (data) => {
    var reviewNode = document.getElementById(data.key);
    reviewNode.parentNode.removeChild(reviewNode);
});

reviews.addEventListener('click', (e) => {
    var reviewNode = e.target.closest('tr');
    //document.getElementById("submit").setAttribute("type", "edit");
    // UPDATE REVEIW
    if (e.target.classList.contains('edit')) {

        nom.value = reviewNode.querySelector('.nom').innerText;
        tel.value = reviewNode.querySelector('.tel').innerText;
        adresse.value = reviewNode.querySelector('.adresse').innerText;
        lat.value = reviewNode.querySelector('.lat').innerText;
        lng.value = reviewNode.querySelector('.lng').innerText;
        nuitjour.value = reviewNode.querySelector('.nuitjour').innerText;
        hiddenId.value = reviewNode.id;
    }

    // DELETE REVEIW
    if (e.target.classList.contains('delete')) {
        var id = reviewNode.id;
        db.ref('ehealth/pharmacie/' + id).remove();

    }
});

function reviewTemplate({ nom, tel, adresse, lat, lng, nuitjour }) {
    return `

    <td class="nom">${nom}</td>
    <td class="tel">${tel}</td>
    <td class="adresse">${adresse}</td>
    <td class="lat">${lat}</td>
    <td class="lng">${lng}</td>
    <td class="nuitjour">${nuitjour}</td>
   <td><button type="button"   class="delete btn btn-danger waves-effect m-r-20" >Supprimer</button>
    <button type="button"   class="edit btn btn-info waves-effect m-r-20" data-toggle="modal" data-target="#defaultModal">Modifier</button>
    </td>
  `
};

//<button class='edit'>Edit</button></td>
/*
`
    <div class='fullName'>${fullName}</div>
    <div class='message'>${message}</div>
    <button class='delete'>Delete</button>
    <button class='edit'>Edit</button>
  `*/