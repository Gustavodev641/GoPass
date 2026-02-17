package com.GoPass.GoPass.service;

import com.GoPass.GoPass.domain.coupon.Coupon;
import com.GoPass.GoPass.domain.coupon.CouponRequestDTO;
import com.GoPass.GoPass.domain.event.Event;
import com.GoPass.GoPass.repositories.CouponRepository;
import com.GoPass.GoPass.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO coupondata) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon coupon = new Coupon();
        coupon.setCode(coupondata.code());
        coupon.setDiscount(coupondata.discount());
        coupon.setValid(new Date(coupondata.valid()));
        coupon.setEvent(event);

        return couponRepository.save(coupon);

    }
}
