[//]: # "Image References"

[image1]: ./images/mvvm-databinding.png "MVVM Databinding"

[image2]: ./images/primary-page.png  "Primary Page"

[image3]: ./images/search-results-for-cat.png  "Search Results for “Cat”"

[image4]: ./images/empty-page.png  "Empty Page"

# Giphy code challenge

This prototype app uses some Architecture Components like ViewModel, LiveData, and other lifecycle-aware classes. Also, it uses the [Data Binding Library](http://developer.android.com/tools/data-binding/guide.html#data_objects) to display data and bind UI elements to actions.

The sample demonstrates an implementation using the [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model–view–viewmodel) (MVVM) architecture.

### Generated APK

You can find the generated APKs [here]().

### What this app does

This a simple application to search on [Giphy](<https://giphy.com/>) gif image resources. For simplicity it only displays **up to 20** search results. Each result is displayed in an item that includes gif's title and gif's image. For gif's content, I selected the `fixed_height_downsampled` image to display.
If there is an issue with network/server (like server does not respond, or network connectivity issue) or there is no data for the search query, user will see the following empty page. It's simple and can be improved later.

|    Primary Page     | Search Results for “Cat” | Empty Page |
| :-----------------: | :----------------------: | :--------: |
| ![alt text][image2] | ![alt text][image3]      |       ![alt text][image4]     |


## System Configuration

I used **Android Studio 3.4** and **Gradle 5.1.1**. The project is fully written in **Kotlin**.

## Designing the app

The ViewModel in the MVVM architecture plays a similar role to the Presenter in the MVP architecture. The two architectures differ in the way that the View communicates with the ViewModel or Presenter respectively:

- When the app modifies the ViewModel in the MVVM architecture, the View is automatically updated by a library or framework. You can’t update the View directly from the ViewModel, as the ViewModel doesn't have access to the necessary reference.

- You can however update the View from the Presenter in an MVP architecture as it has the necessary reference to the View. When a change is necessary, you can explicitly call the View from the Presenter to update it. In this project, you use layout files to bind observable fields in the ViewModel to specific UI elements such as a [TextView](https://developer.android.com/reference/android/widget/TextView.html), or [ImageView](https://developer.android.com/reference/android/widget/ImageView.html). The Data Binding Library ensures that the View and ViewModel remain in sync bi-directionally as illustrated by the following diagram.

  

  ![alt text][image1]

## Implementing the app

In the MVVM architecture, Views react to changes in the ViewModel without being explicitly called. However, the MVVM architecture presents some challenges when working with some Android components.

For example, to show a [`Snackbar`](https://developer.android.com/reference/android/support/design/widget/Snackbar.html), you must use a static call to pass a view object. I implemented it by generating an `extension` functions in `kotlin`.

```java
Snackbar.make(View coordinatorLayout, String text, int length).show();
```

When making use of a Presenter in an MVP architecture, you may call the activity or fragment to delegate responsibility for finding the appropriate view object:

```java
mView.showSnackbar(text)
```

I implemented it by creating an extension function on `View`:

```kotlin
/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        show()
    }
}
```

A ViewModel however, doesn’t have the necessary reference to the activity or fragment. Instead, you can manually subscribe the snackbar to an observable field by making the following changes:

- Creating an `ObservableField<String>` in the ViewModel.
- Establishing a subscription that shows a snackbar when the `ObservableField` changes.

The following code snippet illustrates setting up a subscription between an observable field and a callback which triggers the call to show the snackbar. I implemented it by creating an extension function on a `View`:

```kotlin
/**
 * Triggers a snackbar message when the value contained by snackbarMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}
```

## Discussion

This UX of the application can be improved by having a better error handling, like informing the user about the issue and helping him/her to resolve that. For instance in case of network issue (no Wi-Fi or network connectivity) we can inform user to check the device's internet connection.

We can also add a new data source for device storage data source as `LocalDataSource`. It helps to save gif images and later on use them to save network data usages and also make gifs available offline for the users. Also it helps to load the same gif files much faster.

Also we can add 

## License

[MIT License](/LICENSE).