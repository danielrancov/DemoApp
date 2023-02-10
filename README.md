# DemoApp

## Architecture

I did choose the classic MVVM architecture with clean architecture influences. As it is a small demo app it is split only into 3 parts: 

- data: Here we have the network part, the models, and the repository
- di: Here, we have all the dependency injection-related configurations. I always like to separate this from the rest of the app, if the app 
gets bigger and it's split into modules we can create an entire module just for DI and just make some binding of the data modules.
- UI: This consists of our presentation and view layers.

Besides these three parts, we could add another hard separation on the project, but from my point of view, this depends on every project and in my specific 
case the only thing that would happen would be to duplicate a lot of code without changing anything. I am talking about a dedicated domain layer where we
would add new UI models, mappers, and maybe also use cases. 

## Network

The network data comes from a product's public API that I found, the cool thing about them and I specifically searched for it, it's that they have 
pagination on their APIs. Also, I am saving just the IDs, titles, and thumbnails of the products. I wanted an API that gives us paginated responses
so I can simulate the best I could in a real-life scenario, and also implement infinite scrolling in the recycler view based on that. 
The data is passed then to the repository, which maps it to a flow and emits the values in the form of a **NetworkResult**. 
Dependencies used: Retrofit, Flow, Hilt.

## UI

As described before, this package consists of the presentation and views. The Structure that I choose for starting this new project was Single Activity 
- Multiple fragments, as google describes it as best practice nowadays, with the activity hosting the navigation graph, instantiating a fragment that would
be the start destination, and going forward we can add as many fragments as we want in our navigation graph, also create custom subgraphs, and so on. 

In the ViewModel, we have the state as a LiveData, which will emit new values whenever a new page of products is fetched. When the result comes back we 
map from the **NetworkResult** object to an easier-to-manipulate by the UI object. Also, all the data about the state of the page are saved in the ViewModel, 
so we don't lose any data when the configuration changes, or even worse, for our app to crash. 

In the Fragment, we just have to observe the changes that the ViewModel posts on the LiveData and display them. The UI should be dumb, without any logic.


**Obviously, every project and every team is different, and nothing should be taken as it is. In some projects/teams some architecture/structures may work better
then others.**
