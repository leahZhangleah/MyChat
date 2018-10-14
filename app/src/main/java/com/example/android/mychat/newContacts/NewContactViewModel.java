package com.example.android.mychat.newContacts;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.mychat.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class NewContactViewModel extends ViewModel {
    private static final String TAG = "NewContactViewModel";
    private PublishSubject<String> publishSubject ;
    private CompositeDisposable compositeDisposable;
    private NewContactRepository newContactRepository;
    //private List<User> usersList;
    private SearchResponse response;
    public NewContactViewModel() {
        publishSubject = PublishSubject.create();
        //usersList = new ArrayList<>();
        response = SearchResponse.loading();
        compositeDisposable = new CompositeDisposable();
        newContactRepository = new NewContactRepository();
    }

    //observeon works downstream
    /*
    subscribeon only influences the thread that is used when the Observable is subscribed to and
    it will stay on it downstream.
     */
    public SearchResponse createPublishObservable(String search){
        compositeDisposable.add(publishSubject.debounce(100, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(Schedulers.io())
                .map(new Function<String, List<User>>() {
                    @Override
                    public List<User> apply(String s) throws Exception {
                        return newContactRepository.searchNewContact(s);
                    }
                })
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        //userMutableLiveData.postValue(users);
                        //usersList.clear();
                        //usersList.addAll(users);
                        response = SearchResponse.success(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"There is an error in search user" + throwable.getMessage());
                        response = SearchResponse.error(throwable);
                    }
                }));
        publishSubject.onNext(search);
        return response;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
