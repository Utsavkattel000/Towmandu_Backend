package com.tow.mandu.service;

import com.tow.mandu.model.Provider;
import com.tow.mandu.pojo.RiderPojo;

public interface RiderService {

    RiderPojo save(RiderPojo riderPojo, Provider provider);
}
