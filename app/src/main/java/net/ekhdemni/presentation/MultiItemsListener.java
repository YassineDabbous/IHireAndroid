package net.ekhdemni.presentation;

import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Commun;
import net.ekhdemni.model.models.Forum;
import net.ekhdemni.model.models.Idea;
import net.ekhdemni.model.models.Job;
import net.ekhdemni.model.models.Model;
import net.ekhdemni.model.models.Post;
import net.ekhdemni.model.models.Service;
import net.ekhdemni.model.models.user.User;
import net.ekhdemni.model.models.Work;

import tn.core.presentation.listeners.OnInteractListener;

public interface MultiItemsListener extends OnInteractListener<Model> {
    void onClick(Model item);
    void onClick(Job item);
    void onClick(Idea item);
    void onClick(Work item);
    void onClick(Post item);
    void onClick(Forum item);
    void onClick(User item);
    void onClick(Article item);
    void onClick(Service item);
}
