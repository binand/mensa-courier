package in.radongames.smsreceiver;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.radongames.android.data.ListenableFutureConverter;
import com.radongames.android.data.ObservableCrudRepository;
import com.radongames.core.converters.ListConverter;
import com.radongames.core.exceptions.RepositoryException;
import com.radongames.core.interfaces.Converter;

import java.util.List;

import lombok.NonNull;
import lombok.SneakyThrows;

@Dao
public abstract class SmsDao implements ObservableCrudRepository<SmsEntity, Long> {

    /*
     * Converters that are needed.
     */
    private final Converter<SmsEntity, Long> mEntityLongConverter = new Converter<SmsEntity, Long>() {

        @Override
        public Long forward(SmsEntity entity) {

            return entity.getId();
        }

        @Override
        public SmsEntity backward(Long id) {

            /*
             * Documentation says the return value of UPSERT is:
             * The SQLite row id or -1 if the insertion failed and update is performed
             */
            return id == -1 ? null : findById(id);
        }
    };
    private final ListConverter<SmsEntity, Long> mEntityLongListConverter = new ListConverter<>(mEntityLongConverter);
    private final ListenableFutureConverter<SmsEntity, Long> mEntityLongFutureConverter = new ListenableFutureConverter<>(mEntityLongConverter);
    private final ListenableFutureConverter<List<SmsEntity>, List<Long>> mEntityLongListFutureConverter = new ListenableFutureConverter<>(mEntityLongListConverter);

    /*
     * Methods that are Observable/Asynchronous. Directly from ObservableCrudRepository.
     */
    @Override
    public ListenableFuture<SmsEntity> observableSave(@NonNull SmsEntity entity) {

        ListenableFuture<SmsEntity> saved = mEntityLongFutureConverter.backward(saveInternal(entity));
        return saved == null ? ListenableFutureTask.create(() -> entity) : saved;
    }

    @Override
    public ListenableFuture<List<SmsEntity>> observableSaveAll(@NonNull List<SmsEntity> entities) {

        return mEntityLongListFutureConverter.backward(saveAllInternal(entities));
    }

    @Override
    @Query("SELECT * FROM messages WHERE id = :id")
    public abstract ListenableFuture<SmsEntity> observableFindById(@NonNull Long id);

    @Override
    @Query("SELECT EXISTS(SELECT 1 FROM messages WHERE id = :id)")
    public abstract ListenableFuture<Boolean> observableExistsById(@NonNull Long id);

    @Override
    @Query("SELECT * FROM messages")
    public abstract LiveData<List<SmsEntity>> observableFindAll();

    @Override
    @Query("SELECT * FROM messages WHERE id IN (:ids)")
    public abstract LiveData<List<SmsEntity>> observableFindAllById(@NonNull List<Long> ids);

    @Override
    @Query("SELECT COUNT(id) FROM messages")
    public abstract LiveData<Long> observableCount();

    /*
     * Blocking/Synchronous methods. Inherited from CrudRepository.
     */
    @Override
    @SneakyThrows
    public SmsEntity save(@NonNull SmsEntity entity) throws RepositoryException {

        SmsEntity saved = mEntityLongConverter.backward(saveInternal(entity).get());
        return saved == null ? entity : saved;
    }

    @Override
    @SneakyThrows
    public List<SmsEntity> saveAll(@NonNull List<SmsEntity> entities) {

        return mEntityLongListConverter.backward(saveAllInternal(entities).get());
    }

    @Override
    @Query("SELECT * FROM messages WHERE id = :id")
    public abstract SmsEntity findById(@NonNull Long id);

    @Override
    @Query("SELECT EXISTS(SELECT 1 FROM messages WHERE id = :id)")
    public abstract boolean existsById(@NonNull Long id);

    @Override
    @Query("SELECT * FROM messages")
    public abstract List<SmsEntity> findAll();

    @Override
    @Query("SELECT * FROM messages WHERE id IN (:ids)")
    public abstract List<SmsEntity> findAllById(@NonNull List<Long> ids) throws RepositoryException;

    @Override
    @Query("SELECT COUNT(id) FROM messages")
    public abstract long count();

    @Override
    @Query("DELETE FROM messages WHERE id = :id")
    public abstract void deleteById(@NonNull Long id);

    @Override
    @Delete
    public abstract void delete(@NonNull SmsEntity entity);

    @Override
    @Query("DELETE FROM messages WHERE id IN (:ids)")
    public abstract void deleteAllById(@NonNull List<Long> ids) throws RepositoryException;

    @Override
    @Delete
    public abstract void deleteAll(@NonNull List<SmsEntity> entities) throws RepositoryException;

    @Override
    @Query("DELETE FROM messages")
    public abstract void deleteAll();

    /*
     * Internal methods.
     */
    @Insert
    protected abstract ListenableFuture<Long> saveInternal(@NonNull SmsEntity entity) throws RepositoryException;

    @Insert
    protected abstract ListenableFuture<List<Long>> saveAllInternal(@NonNull List<SmsEntity> entities);
}
