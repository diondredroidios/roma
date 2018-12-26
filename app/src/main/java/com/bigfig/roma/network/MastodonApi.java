/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Roma.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Roma is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Roma; if not,
 * see <http://www.gnu.org/licenses>. */

package com.bigfig.roma.network;

import android.support.annotation.Nullable;

import com.bigfig.roma.entity.AccessToken;
import com.bigfig.roma.entity.Account;
import com.bigfig.roma.entity.AppCredentials;
import com.bigfig.roma.entity.Attachment;
import com.bigfig.roma.entity.Card;
import com.bigfig.roma.entity.Emoji;
import com.bigfig.roma.entity.Instance;
import com.bigfig.roma.entity.MastoList;
import com.bigfig.roma.entity.Notification;
import com.bigfig.roma.entity.Relationship;
import com.bigfig.roma.entity.SearchResults;
import com.bigfig.roma.entity.Status;
import com.bigfig.roma.entity.StatusContext;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MastodonApi {
    String ENDPOINT_AUTHORIZE = "/oauth/authorize";
    String DOMAIN_HEADER = "domain";
    String PLACEHOLDER_DOMAIN = "dummy.placeholder";

    @GET("api/v1/timelines/home")
    Call<List<Status>> homeTimeline(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/timelines/public")
    Call<List<Status>> publicTimeline(
            @Query("local") Boolean local,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/timelines/tag/{hashtag}")
    Call<List<Status>> hashtagTimeline(
            @Path("hashtag") String hashtag,
            @Query("local") Boolean local,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/timelines/list/{listId}")
    Call<List<Status>> listTimeline(
            @Path("listId") String listId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/notifications")
    Call<List<Notification>> notifications(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/notifications")
    Call<List<Notification>> notificationsWithAuth(
            @Header("Authorization") String auth, @Header(DOMAIN_HEADER) String domain);

    @POST("api/v1/notifications/clear")
    Call<ResponseBody> clearNotifications();

    @GET("api/v1/notifications/{id}")
    Call<Notification> notification(@Path("id") String notificationId);

    @Multipart
    @POST("api/v1/media")
    Call<Attachment> uploadMedia(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @PUT("api/v1/media/{mediaId}")
    Call<Attachment> updateMedia(@Path("mediaId") String mediaId,
                                 @Field("description") String description);

    @FormUrlEncoded
    @POST("api/v1/statuses")
    Call<Status> createStatus(
            @Header("Authorization") String auth,
            @Header(DOMAIN_HEADER) String domain,
            @Field("status") String text,
            @Field("in_reply_to_id") String inReplyToId,
            @Field("spoiler_text") String warningText,
            @Field("visibility") String visibility,
            @Field("sensitive") Boolean sensitive,
            @Field("media_ids[]") List<String> mediaIds,
            @Header("Idempotency-Key") String idempotencyKey);

    @GET("api/v1/statuses/{id}")
    Call<Status> status(@Path("id") String statusId);

    @GET("api/v1/statuses/{id}/context")
    Call<StatusContext> statusContext(@Path("id") String statusId);

    @GET("api/v1/statuses/{id}/reblogged_by")
    Call<List<Account>> statusRebloggedBy(
            @Path("id") String statusId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/statuses/{id}/favourited_by")
    Call<List<Account>> statusFavouritedBy(
            @Path("id") String statusId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @DELETE("api/v1/statuses/{id}")
    Call<ResponseBody> deleteStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/reblog")
    Call<Status> reblogStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/unreblog")
    Call<Status> unreblogStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/favourite")
    Call<Status> favouriteStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/unfavourite")
    Call<Status> unfavouriteStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/pin")
    Single<Status> pinStatus(@Path("id") String statusId);

    @POST("api/v1/statuses/{id}/unpin")
    Single<Status> unpinStatus(@Path("id") String statusId);

    @GET("api/v1/accounts/verify_credentials")
    Call<Account> accountVerifyCredentials();

    @FormUrlEncoded
    @PATCH("api/v1/accounts/update_credentials")
    Call<Account> accountUpdateSource(@Nullable @Field("source[privacy]") String privacy,
                                      @Nullable @Field("source[sensitive]") Boolean sensitive);

    @Multipart
    @PATCH("api/v1/accounts/update_credentials")
    Call<Account> accountUpdateCredentials(
            @Nullable @Part(value="display_name") RequestBody displayName,
            @Nullable @Part(value="note") RequestBody note,
            @Nullable @Part(value="locked") RequestBody locked,
            @Nullable @Part MultipartBody.Part avatar,
            @Nullable @Part MultipartBody.Part header,
            @Nullable @Part(value="fields_attributes[0][name]") RequestBody fieldName0,
            @Nullable @Part(value="fields_attributes[0][value]") RequestBody fieldValue0,
            @Nullable @Part(value="fields_attributes[1][name]") RequestBody fieldName1,
            @Nullable @Part(value="fields_attributes[1][value]") RequestBody fieldValue1,
            @Nullable @Part(value="fields_attributes[2][name]") RequestBody fieldName2,
            @Nullable @Part(value="fields_attributes[2][value]") RequestBody fieldValue2,
            @Nullable @Part(value="fields_attributes[3][name]") RequestBody fieldName3,
            @Nullable @Part(value="fields_attributes[3][value]") RequestBody fieldValue3);

    @GET("api/v1/accounts/search")
    Call<List<Account>> searchAccounts(
            @Query("q") String q,
            @Query("resolve") Boolean resolve,
            @Query("limit") Integer limit);

    @GET("api/v1/accounts/{id}")
    Call<Account> account(@Path("id") String accountId);

    /**
     * Method to fetch statuses for the specified account.
     * @param accountId ID for account for which statuses will be requested
     * @param maxId Only statuses with ID less than maxID will be returned
     * @param sinceId Only statuses with ID bigger than sinceID will be returned
     * @param limit Limit returned statuses (current API limits: default - 20, max - 40)
     * @param excludeReplies only return statuses that are no replies
     * @param onlyMedia only return statuses that have media attached
     */
    @GET("api/v1/accounts/{id}/statuses")
    Call<List<Status>> accountStatuses(
            @Path("id") String accountId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit,
            @Nullable @Query("exclude_replies") Boolean excludeReplies,
            @Nullable @Query("only_media") Boolean onlyMedia);

    @GET("api/v1/accounts/{id}/followers")
    Call<List<Account>> accountFollowers(
            @Path("id") String accountId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/accounts/{id}/following")
    Call<List<Account>> accountFollowing(
            @Path("id") String accountId,
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @FormUrlEncoded
    @POST("api/v1/accounts/{id}/follow")
    Call<Relationship> followAccount(@Path("id") String accountId, @Field("reblogs") boolean showReblogs);

    @POST("api/v1/accounts/{id}/unfollow")
    Call<Relationship> unfollowAccount(@Path("id") String accountId);

    @POST("api/v1/accounts/{id}/block")
    Call<Relationship> blockAccount(@Path("id") String accountId);

    @POST("api/v1/accounts/{id}/unblock")
    Call<Relationship> unblockAccount(@Path("id") String accountId);

    @POST("api/v1/accounts/{id}/mute")
    Call<Relationship> muteAccount(@Path("id") String accountId);

    @POST("api/v1/accounts/{id}/unmute")
    Call<Relationship> unmuteAccount(@Path("id") String accountId);

    @GET("api/v1/accounts/relationships")
    Call<List<Relationship>> relationships(@Query("id[]") List<String> accountIds);

    @GET("api/v1/blocks")
    Call<List<Account>> blocks(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/mutes")
    Call<List<Account>> mutes(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/favourites")
    Call<List<Status>> favourites(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @GET("api/v1/follow_requests")
    Call<List<Account>> followRequests(
            @Query("max_id") String maxId,
            @Query("since_id") String sinceId,
            @Query("limit") Integer limit);

    @POST("api/v1/follow_requests/{id}/authorize")
    Call<Relationship> authorizeFollowRequest(@Path("id") String accountId);

    @POST("api/v1/follow_requests/{id}/reject")
    Call<Relationship> rejectFollowRequest(@Path("id") String accountId);

    @FormUrlEncoded
    @POST("api/v1/reports")
    Call<ResponseBody> report(
            @Field("account_id") String accountId,
            @Field("status_ids[]") List<String> statusIds,
            @Field("comment") String comment);

    @GET("api/v1/search")
    Call<SearchResults> search(@Query("q") String q, @Query("resolve") Boolean resolve);

    @FormUrlEncoded
    @POST("api/v1/apps")
    Call<AppCredentials> authenticateApp(
            @Header(DOMAIN_HEADER) String domain,
            @Field("client_name") String clientName,
            @Field("redirect_uris") String redirectUris,
            @Field("scopes") String scopes,
            @Field("website") String website);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<AccessToken> fetchOAuthToken(
            @Header(DOMAIN_HEADER) String domain,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("code") String code,
            @Field("grant_type") String grantType
    );

    @GET("/api/v1/statuses/{id}/card")
    Call<Card> statusCard(
            @Path("id") String statusId
    );

    @GET("/api/v1/lists")
    Call<List<MastoList>> getLists();

    @GET("/api/v1/custom_emojis")
    Call<List<Emoji>> getCustomEmojis();

    @GET("api/v1/instance")
    Call<Instance> getInstance();
}