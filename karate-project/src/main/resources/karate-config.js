function fn() {
  let rawEnvProp = karate.env;
  karate.log('karate.env system property was:', rawEnvProp);
  
  if (!rawEnvProp || rawEnvProp.includes('KARATE_ENV')) {
    rawEnvProp = null;
  }
  const env = rawEnvProp || java.lang.System.getenv('KARATE_ENV') || 'stage';

  let raw;
  try {
    raw = karate.read('classpath:env/config-' + env + '.json');
  } catch(e) {
    karate.log('[config] Arquivo JSON não encontrado para env=' + env + ' -> usando defaults. Erro: ' + e.message);
    raw = {};
  }

  function pick(key, fallback) {
    const sys = java.lang.System.getProperty(key);
    if (sys) return sys;
    const envVar = java.lang.System.getenv(key);
    if (envVar) return envVar;
    if (raw[key] !== undefined && raw[key] !== null) return raw[key];
    return fallback;
  }

  karate.configure('ssl', { trustAll: true });
  karate.configure('connectTimeout', +pick('CONNECT_TIMEOUT', 30000));
  karate.configure('readTimeout', +pick('READ_TIMEOUT', 30000));

  const config = { env: env };

  config.baseUrl = pick("BASE_URL", null);
  config.baseOccUrl = pick("BASE_URL", null) + "/occ/v2/electronics";
  config.tokenUrl = pick("BASE_URL", null) + "/authorizationserver/oauth/token";
  config.authorizeUrl = pick("BASE_URL", null) + "/authorizationserver/oauth/authorize";
  config.loginUrl = pick("BASE_URL", null) + "/authorizationserver/login";
  config.systemGrantType = pick("SYSTEM_TOKEN_GRAN_TYPE", null);
  config.clientId = pick("TOKEN_CLIENT_ID", null);
  config.clientSecret = pick("TOKEN_CLIENT_SECRET", null);
  config.clientEncoded = pick("CLIENT_ENCODED", null);
  config.userUid = pick("USER_UID", null);
  config.userPwd = pick("USER_PASSWORD", null);

  return config;
}