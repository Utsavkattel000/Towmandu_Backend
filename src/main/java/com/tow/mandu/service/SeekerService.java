package com.tow.mandu.service;

import com.tow.mandu.model.Seeker;
import com.tow.mandu.model.User;

public interface SeekerService {
    Seeker getSeekerByUser(User user);
}
