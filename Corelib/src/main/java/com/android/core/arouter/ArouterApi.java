package com.android.core.arouter;

/**
 * Created by lijie on 2019-06-26.
 */
public interface ArouterApi {

    @Route("/activity/main")
    void skipDouBanActivity(@Param("type") int type);
}
