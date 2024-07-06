import React, {useEffect, useState} from 'react';
import FavoritesPage from './FavoritesPage';
import axios from 'axios';
import logo from '../img/310logo.png';
import { useLocation, Link } from 'react-router-dom';
import InactivityTimer from "../components/InactivityTimer";
import { paramsSerializer } from "../components/paramsSerializer";

const ParkSearchAndResultsPage = () => {
    const location = useLocation();
    const [loggedInUser, setLoggedInUser] = useState(null);
    const [searchResults, setSearchResults] = useState([]);
    const [visibleParks, setVisibleParks] = useState([]);
    const [selectedPark, setSelectedPark] = useState(null);
    const [searchInput, setSearchInput] = useState("");
    const [searchType, setSearchType] = useState("park-name");
    const [favoritedParks, setFavoritedParks] = useState([]);
    const [hoveringParkId, setHoveringParkId] = useState(null);

    const updateSearchInput = (newValue) => {
        setSearchInput(newValue);
    };

    const addToFavorites = async (park) => {
        const isAlreadyFavorited = favoritedParks.some((p) => p.id === park.id);

        if (isAlreadyFavorited) {
            alert('This park is already in your favorites list.');
        } else {
            if (!loggedInUser) {
                alert('Please log in to add parks to your favorites.');
                return;
            }

            try {
                console.log(park.fullName);
                console.log(loggedInUser);
                console.log(loggedInUser.username);
                const favoriteRequest = {
                    username: loggedInUser.username, // Using the logged-in user's username
                    parkName: park.fullName, // Using the full name of the park
                    apiId: park.id
                };
                console.log(favoriteRequest);
                const response = await fetch('/addFavorite', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(favoriteRequest),
                });

                if (response.status === 200) {
                    const newFavoritedParks = [...favoritedParks, park];
                    setFavoritedParks(newFavoritedParks);
                    alert('Park added to your favorites list successfully.');
                } else {
                    alert('Failed to add park to favorites.');
                }
            } catch (error) {
                console.error('Error adding park to favorites:', error);
                alert('An error occurred while adding park to favorites.');
            }
        }
    };

    const fetchLogout = async() => {
        const response = await axios.post("api/logout");
        console.log(response.data);
        navigate('/login');
    }

    const performSearch = async (searchParams, newSearchInput = null) => {
        try {
            const response = await axios.get(`http://localhost:8080/searchParks`, {
                params: searchParams,
                paramsSerializer,
            });
            setSearchResults(response.data);
            setVisibleParks(response.data.slice(0, 10));
            setSelectedPark(null);
            if (newSearchInput) {
                updateSearchInput(newSearchInput);
            }
            setSearchType("park-name");
        } catch (error) {
            console.error('There was an error fetching the park data:', error);
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        console.log(searchType);

        if (searchType === "park-name") {
            performSearch({
                parkName: searchInput || ""
            })
        }
        else if (searchType === "park-state") {
            performSearch({
                stateCode: searchInput || ""
            })
        }
        else if (searchType === "park-amenities") {
            performSearch({
                amenities: searchInput ? searchInput.split(",") : [],
            })
        }
        else{ //if (searchType === "park-activities") {
            performSearch({
                activities: searchInput ? searchInput.split(",") : [],
            })
        }
    };

    useEffect(() => {
        const fetchLoggedInUser = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/checkSession');
                setLoggedInUser(response.data); // Directly set the response data as the logged-in user
                console.log(response.data)
                console.log(typeof response.data)
            } catch (error) {
                console.error('Error fetching logged-in user:', error);
            }
        };

        fetchLoggedInUser();
    }, []);

    const loadMoreParks = () => {
        const nextVisibleParks = searchResults.slice(0, visibleParks.length + 10);
        setVisibleParks(nextVisibleParks);
    };

    const showParkDetails = (park) => {
        setSelectedPark(park); // Set the selected park to show its details
    };

    return (
        <div>
            <InactivityTimer timeout={60000} />
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: '20px' }}>
                <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Search for Parks</h1>
                <div style={{ display: 'flex', justifyContent: 'center', gap: '20px' }}>
                    <Link to="/search">
                        <button
                            id="search-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Search
                        </button>
                    </Link>
                    <Link to="/suggest">
                        <button
                            id="suggest-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Suggest
                        </button>
                    </Link>
                    <Link to="/compare">
                        <button
                            id="compare-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Compare
                        </button>
                    </Link>
                    <Link to="/favorites">
                        <button
                            id="favorites-button"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Favorites
                        </button>
                    </Link>
                    <Link to="/login">
                        <button
                            id="logout-button"
                            onClick={() => {
                                fetchLogout();
                            }}
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                            }}
                        >
                            Log Out
                        </button>
                    </Link>
                </div>
            </div>
            <div style={{ backgroundColor: '#f8f9fa', padding: '15px 0' }}>
                <div className="container">
                    <form onSubmit={handleSearch} className="form-inline d-flex justify-content-between align-items-center">
                        <img src={logo} alt="logo" style={{ height: '75px', marginRight: '20px' }} />
                        <input
                            type="text"
                            value={searchInput}
                            onChange={(e) => setSearchInput(e.target.value)}
                            className="form-control mr-sm-2"
                            placeholder="Search..."
                            id={"search-input"}
                            style={{ margin: '0 5px', flex: '1' }}
                        />
                        <div className={"d-flex"}>
                            <div className={"row flex-column me-1 ms-1"}>
                                <div className={"mb-1"}>
                                    <input type={"radio"}
                                           name={"search-type"}
                                           id={"search-park-name"}
                                           value={"park-name"}
                                           defaultChecked
                                           onClick={() => setSearchType("park-name")}/>
                                    <label htmlFor={"search-park-name"}>Park Name</label>
                                </div>
                                <div>
                                    <input type={"radio"}
                                           name={"search-type"}
                                           id={"search-park-state"}
                                           value={"park-state"}
                                           onClick={() => setSearchType("park-state")}/>
                                    <label htmlFor={"search-park-state"}>Park State</label>
                                </div>
                            </div>
                            <div className={"row flex-column me-1"}>
                                <div className={"mb-1"}>
                                    <input type={"radio"}
                                           name={"search-type"}
                                           id={"search-park-amenities"}
                                           value={"park-amenities"}
                                           onClick={() => setSearchType("park-amenities")}/>
                                    <label htmlFor={"search-park-amenities"}>Park Amenities</label>
                                </div>
                                <div>
                                    <input type={"radio"}
                                           name={"search-type"}
                                           id={"search-park-activities"}
                                           value={"park-activities"}
                                           onClick={() => setSearchType("park-activities")}/>
                                    <label htmlFor={"search-park-activities"}>Park Activities</label>
                                </div>
                            </div>
                        </div>
                        <button type="submit"
                                className="btn"
                                id={"search-button-input"}
                                style={{
                                    padding: '6px 12px',
                                    background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                    color: 'white',
                                    border: 'none'
                                }}>Search</button>
                    </form>
                </div>
            </div>
            {visibleParks.length > 0 && (
                            <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', padding: '0 5%' }}
                                 className="results-container">
                                {visibleParks.map((park, index) => (
                                    <div
                                        key={index}
                                        id={`${JSON.stringify(park)}`}
                                        style={{
                                            width: '40%',
                                            margin: '20px',
                                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                                            borderRadius: '10px',
                                            overflow: 'hidden',
                                            textAlign: 'center',
                                            background: '#fff',
                                            cursor: 'pointer',
                                            position: 'relative',
                                            transition: 'transform 0.3s',
                                        }}
                                        onClick={() => showParkDetails(park)}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.transform = 'scale(1.02)';
                                            setHoveringParkId(park.id); // Set the hoveringParkId state
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.transform = 'scale(1)';
                                            setHoveringParkId(null); // Reset the hoveringParkId state
                                        }}
                                    >
                                        <img
                                            src={park.images[0]?.url}
                                            alt={park.images[0]?.altText}
                                            style={{
                                                width: '100%',
                                                height: '200px',
                                                objectFit: 'cover',
                                                borderRadius: '10px 10px 0 0',
                                            }}
                                        />
                                        <div style={{ padding: '10px' }}>
                                            <h3>{park.fullName}</h3>
                                            <p>{park.description}</p>
                                        </div>
                                        {/* Render the "plus" button only when the specific park card is being hovered over */}
                                        {hoveringParkId === park.id && (
                                            // For the "Add to Favorites" button
                                            <div
                                                style={{
                                                    position: 'absolute',
                                                    top: '10px',
                                                    right: '10px',
                                                    backgroundColor: 'rgba(255, 255, 255, 0.7)',
                                                    borderRadius: '50%',
                                                    width: '30px',
                                                    height: '30px',
                                                    display: 'flex',
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    cursor: 'pointer',
                                                }}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    addToFavorites(park);
                                                }}
                                                role="button"
                                                aria-label="Add to Favorites"
                                                id="addToFavoritesButton"
                                            >
                                                <span style={{ color: 'white', fontSize: '24px' }}>+</span>
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        )}

            {(visibleParks.length < searchResults.length) && (
                <div style={{textAlign: 'center', margin: '20px 0'}}>
                    <button onClick={loadMoreParks}
                            id={"load-more"}
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px'
                            }}>Load More
                    </button>
                </div>
            )}
            {location.pathname === '/favorites' && (
                <FavoritesPage favoritedParks={favoritedParks} />
            )}
            {selectedPark && (
                            <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                                <div
                                    id="park-details"
                                    className="specific-result"
                                    style={{
                                        position: 'relative',
                                        width: '60%',


                                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                                        borderRadius: '10px',
                                        textAlign: 'center',
                                        background: 'white',
                                        padding: '20px',
                                    }}
                                >
                                    <button
                                        id="exit-button"
                                        onClick={() => setSelectedPark(null)}
                                        style={{
                                            position: 'absolute',
                                            top: '10px',
                                            right: '10px',
                                            padding: '5px 10px',
                                            background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '5px',
                                            cursor: 'pointer',
                                            fontSize: '14px',
                                        }}
                                    >
                                        X
                                    </button>
                                    <img
                                        src={selectedPark.images[0]?.url}
                                        alt={selectedPark.images[0]?.altText || 'Park image'}
                                        style={{ width: 'calc(100% - 40px)', margin: '20px', borderRadius: '10px' }}
                                    />
                                    {selectedPark && (
                                        <p>
                                            <em>
                                                {favoritedParks.some((p) => p.id === selectedPark.id)
                                                    ? 'This park is in your favorites.'
                                                    : 'This park is not in your favorites.'}
                                            </em>
                                        </p>
                                    )}
                                    <div className="park-name-close" onClick={() => setSelectedPark(null)}>
                                        <h2>{selectedPark.fullName}</h2>
                                    </div>
                                    <p id="park-description">{selectedPark.description}</p>
                                    <p id="park-location">
                                        Location: {selectedPark.addresses[0]?.city},{' '}
                                        {selectedPark.states.split(',').map((state, index) => (
                                            <button
                                                key={index}
                                                id="state-button"
                                                onClick={() => performSearch({ stateCode: [state.trim()] }, state.trim())}
                                                style={{ marginRight: '5px' }}
                                            >
                                                {state.trim()}
                                            </button>
                                        ))}
                                    </p>
                                    <p id="park-url">URL: <a href={selectedPark.url} target="_blank" rel="noopener noreferrer">{selectedPark.url}</a></p>
                                    <p id="entrance-fee">Entrance Fee: {selectedPark.entranceFees[0]?.cost ? `$${selectedPark.entranceFees[0]?.cost}` : 'Free'}</p>
                                    <p id="amenities">
                                        Amenities:{' '}
                                        {selectedPark.topics.map((topic, index) => (
                                            <button
                                                key={index}
                                                id="amenities-button"
                                                onClick={() => performSearch({ amenities: [topic.name] }, topic.name)}
                                                style={{ marginRight: '5px' }}
                                            >
                                                {topic.name}
                                            </button>
                                        ))}
                                    </p>
                                    <p id="activities">
                                        Activities:{' '}
                                        {selectedPark.activities.map((activity, index) => (
                                            <button
                                                key={index}
                                                id="activity-button"
                                                onClick={() => performSearch({ activities: [activity.name] }, activity.name)}
                                                style={{ marginRight: '5px' }}
                                            >
                                                {activity.name}
                                            </button>
                                        ))}
                                    </p>
                                    <button
                                        onClick={() => setSelectedPark(null)}
                                        id="back-to-search-results"
                                        style={{
                                            padding: '10px 20px',
                                            background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                            color: 'white',
                                            border: 'none',
                                            borderRadius: '5px',
                                            cursor: 'pointer',
                                            fontSize: '16px',
                                            marginTop: '20px'
                                        }}
                                    >
                                        Back to Search Results
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                );
            };

export default ParkSearchAndResultsPage;