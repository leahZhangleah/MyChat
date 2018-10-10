package com.example.android.mychat.newContacts;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.mychat.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class NewContactViewModel extends ViewModel {
    private static final String TAG = "NewContactViewModel";
    private PublishSubject<String> publishSubject ;
    private CompositeDisposable compositeDisposable;
    private NewContactRepository newContactRepository;
    private MutableLiveData<List<User>> userMutableLiveData;
    public NewContactViewModel() {
        publishSubject = PublishSubject.create();
        userMutableLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        newContactRepository = new NewContactRepository();
    }

    public MutableLiveData<List<User>> createPublishObservable(String search){
        compositeDisposable.add(publishSubject.debounce(800, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        if(s.isEmpty()|| TextUtils.equals(s,"")){
                            return false;
                        }
                        return true;
                    }
                })
                .distinctUntilChanged()
                .switchMap(new Function<String, ObservableSource<List<User>>>() {
                    @Override
                    public ObservableSource<List<User>> apply(String s) throws Exception {
                        //todo: viewmodel's search method
                        return newContactRepository.searchNewContact(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        userMutableLiveData.postValue(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"There is an error in search user" + throwable.getMessage());
                    }
                }));
        publishSubject.onNext(search);
        return userMutableLiveData;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
