package com.example.mapsappogg.models


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val db = FirebaseFirestore.getInstance()
    private val markersCollection = "Markers"
    fun saveMarker(newMarker: Marker) {
        db.collection(markersCollection)
            .add(
                hashMapOf(
                    "userId" to newMarker.userId,
                    "markerLatitude" to newMarker.position.latitude,
                    "markerLongitude" to newMarker.position.longitude,
                    "markerTitle" to newMarker.title,
                    "markerSnippet" to newMarker.snippet,
                    "markerColor" to newMarker.color,
                    "markerPhoto" to newMarker.photo
                )
            )
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DOCUMENT SNAPSHOT ADDED WITH ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "ERROR TO ADD DOCUMENT!", e)
            }
    }

    fun editMarker(editedMarker: Marker) {
        db.collection(markersCollection).document(editedMarker.markerId!!)
            .set(
                hashMapOf(
                    "markerLatitude" to editedMarker.position.latitude,
                    "markerLongitude" to editedMarker.position.longitude,
                    "markerTitle" to editedMarker.title,
                    "markerSnippet" to editedMarker.snippet,
                    "markerColor" to editedMarker.color,
                    "markerPhoto" to editedMarker.photo,
                    "userId" to editedMarker.userId
                )
            )
    }

    fun deleteMarker(editedMarker: Marker) {
        db.collection(markersCollection).document(editedMarker.markerId!!)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "SUCCESSFULLY DELETED DOCUMENT SNAPSHOT!") }
            .addOnFailureListener { e -> Log.w(TAG, "ERROR TO DELETE DOCUMENT!", e) }
    }

    fun getMarkers(): CollectionReference {
        return db.collection(markersCollection)
    }
}