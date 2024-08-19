package com.example.demo.controller.base;

import com.example.demo.service.base.BaseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractController<S extends BaseService> implements BaseController {
    public final S service;
    protected static final String API = "/api";
    protected static final String VERSION = "/v1";
    public static final String PATH = API+VERSION;


}
