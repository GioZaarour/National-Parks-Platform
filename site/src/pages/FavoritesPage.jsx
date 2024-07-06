import React, { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import axios from "axios";
import InactivityTimer from "../components/InactivityTimer";
import { paramsSerializer} from "../components/paramsSerializer";

const FavoritesPage = () => {
    const [favoritedParks, setFavoritedParks] = useState([]);
    const [hoveringParkId, setHoveringParkId] = useState(null);
    const [showRemoveConfirmation, setShowRemoveConfirmation] = useState(false);
    const [parkToRemove, setParktToRemove] = useState(null);
    const [selectedPark, setSelectedPark] = useState(null);
    const [isPrivate, setIsPrivate] = useState(true);
    const [username, setUsername] = useState("");
    const [showDeleteAllConfirmation, setShowDeleteAllConfirmation] = useState(false);

   const getParks = async() => {
       try {
           const userResponse = await axios.get('/api/checkSession');
           console.log(userResponse);
           console.log("Server response:", userResponse.data);
           const parkResponse = await axios.get(`/fetchUserFavoritesAndReturnFavoriteObject?username=${userResponse.data.username}`);
           console.log(parkResponse);
           let allParkResults = [];
           for (let park of parkResponse.data) {
               console.log(park);
               console.log(park.apiId);
               const parkResult = await axios.get("/searchParks", {
                   params: {parkName: park.apiId},
                   paramsSerializer
               });
               allParkResults.push([parkResult.data[0], park.id])
           }
           const privacyResponse= await axios.get(`/isListPrivate?username=${userResponse.data.username}`);
           if (privacyResponse.status === 200) {
               setIsPrivate(privacyResponse.data)
           }
           else {
               console.log("error fetching privacy status");
           }
           console.log(allParkResults);
           setUsername(userResponse.data.username);
           setFavoritedParks(allParkResults)
       } catch (error) {
           console.log("unable to fetch user or parks", error);
       }
   }

   useEffect(() => {
       getParks()
   }, []);


    const togglePrivacy = async() => {
        const params = new URLSearchParams();
        params.append('username', username);
        params.append("isPrivate", !isPrivate);

        const response = await axios.post('/setPrivate', params);
        if (response.status === 200) {
            setIsPrivate(!isPrivate)
        }
    };

    if (!favoritedParks) {
        return <div>Loading...</div>;
    }

    const removeFavorite = (index) => {
        setParktToRemove(index);
        setShowRemoveConfirmation(true);
    };

    const confirmRemoveFavorite = async () => {
        console.log(parkToRemove);
        const response = await axios.post('/removeFavorite', JSON.stringify({ username: username }), {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.status === 200) {
            setParktToRemove(null);
            setShowRemoveConfirmation(false);
            getParks();
        }
    };

    const cancelRemoveFavorite = () => {
        setShowRemoveConfirmation(false);
        setParktToRemove(null);
    };

    const showParkDetails = (park) => {
        setSelectedPark(park);
    };

    const backToFavorites = () => {
        setSelectedPark(null);
    }

    const moveUp = async(index) => {
        if (index > 0) {
            console.log(index);
            console.log(favoritedParks);
            console.log(favoritedParks[index][0]);
            const response = await axios.post('/updateFavorite', {
                username: username,
                parkName: favoritedParks[index][0].fullName,
                ranking: index - 1,
                apiId: favoritedParks[index][0].id,
            });
            console.log(response);
            if (response.status === 200) {
                getParks();
            }
        }
    };

    const moveDown = async(index) => {
        if (index < favoritedParks.length - 1) {
            console.log(favoritedParks[index][0].fullName)
            const response = await axios.post('/updateFavorite', {
                username: username,
                parkName: favoritedParks[index][0].fullName,
                ranking: index + 2,
                apiId: favoritedParks[index][0].id,
            });
            console.log(response);
            if (response.status === 200) {
                getParks();
            }
        }
    };

    const deleteAllFavorites = async () => {
        try {
            const response = await axios.post('/deleteAllFavorites', { username: username });
            if (response.status === 200) {
                setFavoritedParks([]);
                setShowDeleteAllConfirmation(false);
            }
        } catch (error) {
            console.log('Unable to delete all favorites', error);
        }
    };

    const cancelDeleteAllFavorites = () => {
      setShowDeleteAllConfirmation(false);
    };

    if (!favoritedParks) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <InactivityTimer timeout={60000} />
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>My Favorite Parks</h1>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: '20px' }}>
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
                            id="favorties-button"
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
            { username && (
                <div style={{

                    display: 'flex',
                    flexDirection: 'row-reverse',
                    alignItems: 'center',
                    marginBottom: '20px'
                }}>
                    <button
                        id="toggle-button"
                        onClick={togglePrivacy}
                        style={{
                            marginRight: '20px',
                            padding: '10px 20px',
                            backgroundColor: isPrivate ? '#f44336' : '#4CAF50',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                        }}
                    >
                        {isPrivate ? 'Private' : 'Public'}
                    </button>
                    <button
                                      id="delete-all-button"
                                      onClick={() => setShowDeleteAllConfirmation(true)}
                                      style={{
                                        padding: '10px 20px',
                                        backgroundColor: '#f44336',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '5px',
                                        cursor: 'pointer',
                                        marginTop: '20px',
                                      }}
                                    >
                                      Delete All Favorites
                                    </button>
                </div>
            )}

            {favoritedParks.length > 0 ? (
                <div id="favorites-container" className="favorite-park" style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', padding: '0 5%' }}>
                    {favoritedParks.map(([park, id], index) => (
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
                            onMouseEnter={() => setHoveringParkId(park.id)}
                            onMouseLeave={() => setHoveringParkId(null)}
                        >
                            <img
                                src={park.images[0]?.url}
                                alt={park.images[0]?.altText}
                                style={{ width: '100%', height: '200px', objectFit: 'cover', borderRadius: '10px 10px 0 0' }}
                            />
                            <div style={{ padding: '10px' }}>
                                <h3>{park.fullName}</h3>
                                <p>{park.description.length > 170 ? `${park.description.substring(0, 167)}...` : park.description}</p>
                            </div>
                            {hoveringParkId === park.id && (
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
                                    id="remove-favorite-button"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        removeFavorite(id);
                                    }}
                                >
                                    <span style={{ color: 'red', fontSize: '24px' }}>x</span>
                                </div>
                            )}
                            {hoveringParkId === park.id && (
                                <div
                                    style={{
                                        position: 'absolute',
                                        top: '10px',
                                        left: '10px',
                                        display: 'flex',
                                        flexDirection: 'column',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        backgroundColor: 'rgba(255, 255, 255, 0.7)',
                                        borderRadius: '50%',
                                        width: '30px',
                                        height: '30px',
                                        cursor: 'pointer',
                                    }}
                                >
                                    <div
                                        id="up-arrow"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            moveUp(index);
                                        }}
                                        style={{ color: 'green', fontSize: '20px' }}
                                    >
                                        &#8593;
                                    </div>
                                    <div
                                        id="down-arrow"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            moveDown(index);
                                        }}
                                        style={{ color: 'green', fontSize: '20px' }}
                                    >
                                        &#8595;
                                    </div>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            ) : (
                <p style={{ textAlign: 'center' }}>You haven't added any favorite parks yet.</p>
            )}

            {showRemoveConfirmation && (
                <div
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        zIndex: 1000,
                    }}
                >
                    <div
                        style={{
                            backgroundColor: 'white',
                            padding: '20px',
                            borderRadius: '10px',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            textAlign: 'center',
                        }}
                    >
                        <h3>Are you sure you want to remove this park from favorites?</h3>
                        <div style={{ marginTop: '20px' }}>
                            <button
                                id="confirm-remove"
                                onClick={confirmRemoveFavorite}
                                style={{
                                    marginRight: '10px',
                                    padding: '10px 20px',
                                    backgroundColor: '#4CAF50',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '5px',
                                    cursor: 'pointer',
                                }}
                            >
                                Yes
                            </button>
                            <button
                                onClick={cancelRemoveFavorite}
                                style={{
                                    padding: '10px 20px',
                                    backgroundColor: '#f44336',
                                    color: 'white',
                                    border: 'none',
                                    borderRadius: '5px',
                                    cursor: 'pointer',
                                }}
                            >
                                No
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {showDeleteAllConfirmation && (
              <div
                style={{
                  position: 'fixed',
                  top: 0,
                  left: 0,
                  width: '100%',
                  height: '100%',
                  backgroundColor: 'rgba(0, 0, 0, 0.5)',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  zIndex: 1000,
                }}
              >
                <div
                  style={{
                    backgroundColor: 'white',
                    padding: '20px',
                    borderRadius: '10px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    textAlign: 'center',
                  }}
                >
                  <h3>Are you sure you want to delete all favorite parks?</h3>
                  <div style={{ marginTop: '20px' }}>
                    <button
                      id="confirm-delete-all"
                      onClick={deleteAllFavorites}
                      style={{
                        marginRight: '10px',
                        padding: '10px 20px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                      }}
                    >
                      Yes
                    </button>
                    <button
                      onClick={cancelDeleteAllFavorites}
                      style={{
                        padding: '10px 20px',
                        backgroundColor: '#f44336',
                        color: 'white',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                      }}
                    >
                      No
                    </button>
                  </div>
                </div>
              </div>
            )}

            {selectedPark && (
                <div
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        zIndex: 1000,
                    }}
                    onClick={() => setSelectedPark(null)}
                >
                    <div
                        className="specific-result"
                        style={{
                            position: 'relative',
                            width: '60%',
                            maxHeight: '80vh',
                            overflowY: 'auto',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            borderRadius: '10px',
                            textAlign: 'center',
                            background: 'white',
                            padding: '20px',
                            zIndex: 1001,
                        }}
                        onClick={(e) => e.stopPropagation()}
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
                        <h2>{selectedPark.fullName}</h2>
                        <p id="park-description">{selectedPark.description}</p>
                        <p id="park-location">
                            Location: {selectedPark.addresses[0]?.city}, {selectedPark.states}
                        </p>
                        <p id="park-url">
                            URL: <a href={selectedPark.url} target="_blank" rel="noopener noreferrer">{selectedPark.url}</a>
                        </p>
                        <p id="entrance-fee">
                            Entrance Fee: {selectedPark.entranceFees[0]?.cost ? `$${selectedPark.entranceFees[0]?.cost}` : 'Free'}
                        </p>
                        <p id="amenities">
                            Amenities:{' '}
                            {selectedPark.topics.map((topic, index) => (
                                <button key={index} style={{ marginRight: '5px' }}>
                                    {topic.name}
                                </button>
                            ))}
                        </p>
                        <p id="activities">
                            Activities:{' '}
                            {selectedPark.activities.map((activity, index) => (
                                <button key={index} style={{ marginRight: '5px' }}>
                                    {activity.name}
                                </button>
                            ))}
                        </p>
                        <button
                            onClick={() => setSelectedPark(null)}
                            id="back-to-favorites"
                            style={{
                                padding: '10px 20px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px',
                                marginTop: '20px',
                            }}
                        >
                            Back to Favorites
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default FavoritesPage;