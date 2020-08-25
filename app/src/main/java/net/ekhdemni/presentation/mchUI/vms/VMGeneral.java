package net.ekhdemni.presentation.mchUI.vms;

import net.ekhdemni.domain.usecase.UCGeneral;
import net.ekhdemni.model.models.Alert;
import net.ekhdemni.model.models.App;
import net.ekhdemni.model.models.Broadcast;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.model.models.Country;
import net.ekhdemni.model.models.Notification;
import net.ekhdemni.model.models.Relation;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.model.models.Service;
import net.ekhdemni.presentation.base.MyViewModel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class VMGeneral extends MyViewModel<Boolean> {

    public MutableLiveData<List<App>> apps = new MutableLiveData<>();
    public MutableLiveData<List<Alert>> alerts = new MutableLiveData<>();
    public MutableLiveData<List<Broadcast>> broadcasts = new MutableLiveData<>();
    public MutableLiveData<List<Resource>> resources = new MutableLiveData<>();
    public MutableLiveData<List<Service>> services = new MutableLiveData<>();
    public MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    public MutableLiveData<List<Country>> countries = new MutableLiveData<>();
    public MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    public MutableLiveData<List<Relation>> relations = new MutableLiveData<>();
    public MutableLiveData<Relation> relation = new MutableLiveData<>();

    public void apps() {
        new UCGeneral().apps(this.getGenericClosure(apps));
    }
    public void alerts() {
        new UCGeneral().alerts(this.getGenericClosure(alerts));
    }
    public void broadcasts() {
        new UCGeneral().broadcasts(this.getGenericClosure(broadcasts));
    }
    public void resources() {
        new UCGeneral().resources(this.getGenericClosure(resources));
    }
    public void services(Integer id) {
        new UCGeneral().services(id, this.getGenericClosure(services));
    }
    public void categories(int id) {
        new UCGeneral().categories(id, this.getGenericClosure(categories));
    }
    public void countries() {
        new UCGeneral().countries(this.getGenericClosure(countries));
    }
    public void notifications() {
        new UCGeneral().notifications(this.getGenericClosure(notifications));
    }


    public void relations(int id) {
        new UCGeneral().relations(id, this.getGenericClosure(relations));
    }
    public void acceptRelation(int id){
        new UCGeneral().relationsAccept(id, getGenericClosure(relation));
    }
    public void refuseRelation(int id){
        new UCGeneral().relationsRefuse(id, getGenericClosure(relation));
    }



}