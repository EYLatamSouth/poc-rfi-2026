package br.com.poccore.dao;

import br.com.poccore.model.FeatureFlagModel;

import java.util.List;

public interface FeatureFlagDao {
    List<FeatureFlagModel> findFeatureFlagByKey(String name);
}
