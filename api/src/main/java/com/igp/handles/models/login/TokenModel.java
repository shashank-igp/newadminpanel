package com.igp.handles.models.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = TokenModel.Builder.class)
public class TokenModel implements  Cloneable
{
    private String						token;
    private long                        userId;
    private boolean                     expired;

    private TokenModel(Builder builder) {
        token = builder.token;
        userId = builder.userId;
        expired = builder.expired;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private String token;
        private long userId;
        private boolean expired;

        public Builder() {
        }

        public Builder token(String val) {
            token = val;
            return this;
        }

        public Builder userId(long val) {
            userId = val;
            return this;
        }

        public Builder expired(boolean val) {
            expired = val;
            return this;
        }

        public TokenModel build() {
            return new TokenModel(this);
        }
    }
}
