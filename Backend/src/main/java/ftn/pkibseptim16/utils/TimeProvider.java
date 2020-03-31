package ftn.pkibseptim16.utils;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class TimeProvider implements Serializable {

    private static final long serialVersionUID = -1695096439750435982L;

    public Date now() {
        return new Date();
    }
}