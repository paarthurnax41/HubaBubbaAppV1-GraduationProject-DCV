package com.example.hubabubbaapp.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteData: RemoteData,
    localData: LocalData
) {
    val remote = remoteData
    val local = localData
}