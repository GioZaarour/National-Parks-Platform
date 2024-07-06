import React, {useEffect, useState} from 'react';
import axios from 'axios';
import logo from '../img/310logo.png';
import InactivityTimer from "../components/InactivityTimer";
import {Link} from "react-router-dom"; // Assuming you have a logo to use

const CompareFavoritesPage = () => {
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedEntity, setSelectedEntity] = useState(null);
    const [comparisonResults, setComparisonResults] = useState([]);
    const [visibleCard, setVisibleCard] = useState(null);
    const [selectedPark, setSelectedPark] = useState(null);
    const [entityNotFound, setEntityNotFound] = useState(false);
    const [parkDetails, setParkDetails] = useState({}); // Store fetched park details
    const [loggedInUser, setLoggedInUser] = useState(null);

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

    const handleSearch = async () => {
        const usernames = searchTerm.split(',').map(name => name.trim()).filter(name => name.length);

        if (usernames.length === 0) {
            setEntityNotFound(true);
            return;
        }

        let foundUsers = [];
        let privateUsers = [];
        let notFoundUsers = [];
        setEntityNotFound(false);

        for (let i = 0; i < usernames.length; i++) {
            try {
                const response = await axios.get(`http://localhost:8080/getUserByUsername`, { params: { username: usernames[i] } });
                if (response.status === 200) {
                    const isPrivateResponse = await axios.get(`http://localhost:8080/isListPrivate`, { params: { username: usernames[i] } });
                    if (isPrivateResponse.data) {
                        privateUsers.push(usernames[i]);
                    } else {
                        foundUsers.push(response.data.username);
                    }
                }
            } catch (error) {
                console.error('Error fetching user details for:', usernames[i], error);
                if (error.response && error.response.status === 404) {
                    notFoundUsers.push(usernames[i]);
                }
            }
        }

        if (foundUsers.length > 0) {
            setSelectedEntity({ members: foundUsers, displayName: foundUsers.join(', ') });
        } else {
            setSelectedEntity(null);
            setEntityNotFound(true);
        }

        if (privateUsers.length > 0) {
            alert(`The following users have private lists and cannot be included in the suggestion: ${privateUsers.join(', ')}`);
        }

        if (notFoundUsers.length > 0) {
            alert(`The following users do not exist: ${notFoundUsers.join(', ')}`);
        }

        setSearchTerm('');
        setSuggestedPark(null);
    };

    const handleCompare = async () => {
        console.log(loggedInUser);
        console.log(loggedInUser.username);
        if (!selectedEntity || !loggedInUser) return;

        let usersInGroup = [loggedInUser]; // Include the logged-in user for comparison

        // Fetch details for the group members or the selected individual
        try {
            const responses = await Promise.all(selectedEntity.members.map(username =>
                axios.get(`http://localhost:8080/getUserByUsername`, { params: { username } })
            ));
            const usersData = responses.map(response => response.data);
            usersInGroup = usersInGroup.concat(usersData.filter(user => user != null));
        } catch (error) {
            console.error('Error fetching user details:', error);
        }

        // Fetch favorite parks for each user
        try {
            const favoritesResponses = await Promise.all(usersInGroup.map(user =>
                axios.get(`http://localhost:8080/fetchUserFavorites`, { params: { username: user.username } })
            ));
            const favoritesData = favoritesResponses.map(response => response.data);
            usersInGroup.forEach((user, index) => {
                user.favorites = favoritesData[index];
            });
        } catch (error) {
            console.error('Error fetching favorite parks:', error);
        }

        const allParkCodes = new Set();
        usersInGroup.forEach(user => user.favorites.forEach(park => allParkCodes.add(park)));

        const parkCounts = {};
        usersInGroup.forEach(user => {
            user.favorites.forEach(park => {
                parkCounts[park] = (parkCounts[park] || 0) + 1;
            });
        });

        const parkCodes = Array.from(allParkCodes);

        // Fetch details for all parks
        try {
            const parkDetailsResponses = await Promise.all(parkCodes.map(parkCode =>
                axios.get(`/searchParks`, { params: { parkCode } })
            ));
            const parkDataResponses = parkDetailsResponses.map(response => response.data);

            const newParkDetails = {};
            parkDataResponses.forEach(data => {
                if (data.length > 0) {
                    const parkData = data[0];
                    newParkDetails[parkData.parkCode] = {
                        parkCode: parkData.parkCode,
                        parkName: parkData.fullName,
                        images: parkData.images.slice(0, 3),
                        description: parkData.description,
                        location: `${parkData.addresses[0]?.city}, ${parkData.addresses[0]?.stateCode}`,
                        url: parkData.url,
                        entranceFees: parkData.entranceFees,
                        topics: parkData.topics,
                        activities: parkData.activities
                    };
                }
            });
            setParkDetails(newParkDetails);

            // Prepare comparison results including park details
            const comparisonResults = parkCodes.map(parkCode => {
                const parkData = newParkDetails[parkCode];
                const favoritedBy = usersInGroup.filter(user => user.favorites.includes(parkCode)).map(user => user.username);
                return {
                    parkCode,
                    parkName: parkData ? parkData.parkName : parkCode,
                    imageUrl: parkData ? parkData.images[0]?.url : undefined,
                    ratio: `${parkCounts[parkCode]}:${usersInGroup.length}`,
                    favoritedBy
                };
            });

            // Sort comparison results
            comparisonResults.sort((a, b) => {
                const ratioDifference = b.ratio.localeCompare(a.ratio);
                if (ratioDifference !== 0) return ratioDifference;
                return a.parkName.localeCompare(b.parkName);
            });

            setComparisonResults(comparisonResults);
        } catch (error) {
            console.error('Error fetching park details:', error);
        }
    };



    const handleCardVisibility = (parkCode) => {
        if (visibleCard && visibleCard.parkCode === parkCode) {
            // If the same ratio card is already open, close it
            setVisibleCard(null);
        } else {
            // If a different ratio card is clicked or none is open, show the ratio card
            const result = comparisonResults.find(result => result.parkCode === parkCode);
            setVisibleCard(result);
        }
        // Ensure the park details are closed when opening the ratio card
        setSelectedPark(null);
    };

    const handleCloseCard = () => {
        setVisibleCard(null);
    };

    const fetchParksDetails = async (parkCodes) => {
        try {
            const parkDetailsResponses = await Promise.all(parkCodes.map(parkCode =>
                axios.get(`http://localhost:8080/searchParks`, { params: { parkCode } })
            ));
            const newParkDetails = {};
            parkDetailsResponses.forEach(response => {
                const data = response.data;
                if (data.length > 0) {
                    const parkData = data[0];
                    newParkDetails[parkData.parkCode] = {
                        parkCode: parkData.parkCode,
                        parkName: parkData.fullName,
                        images: parkData.images.slice(0, 3),
                        description: parkData.description,
                        location: `${parkData.addresses[0]?.city}, ${parkData.addresses[0]?.stateCode}`,
                        url: parkData.url,
                        entranceFees: parkData.entranceFees,
                        topics: parkData.topics,
                        activities: parkData.activities
                    };
                }
            });
            setParkDetails(newParkDetails);
        } catch (error) {
            console.error('Error fetching park details in batch:', error);
        }
    };

    const fetchParkDetails = async (parkCode) => {
        try {
            const response = await axios.get(`http://localhost:8080/searchParks`, { params: { parkCode } });
            if (response.data.length > 0) {
                const parkData = response.data[0];
                setSelectedPark(parkData); // Now, this will have all the details including fees, amenities, and activities
            } else {
                console.log('No park details found for:', parkCode);
                setSelectedPark(null);
            }
        } catch (error) {
            console.error('There was an error fetching the park data:', error);
            setSelectedPark(null);
        }
    };

    const handleParkClick = (parkCode) => {
        if (selectedPark && selectedPark.parkCode === parkCode) {
            // If the same park details card is already open, close it
            setSelectedPark(null);
        } else {
            // If a different park details card is clicked or none is open, fetch the details
            fetchParkDetails(parkCode);
        }
        // Ensure the ratio card is closed when opening the park details
        setVisibleCard(null);
    };


    const handleCloseParkDetails = () => {
        setSelectedPark(null);
    };

    return (
        <div>
            <InactivityTimer timeout={60000} />
            <h1 style={{ textAlign: 'center', marginBottom: '20px' }}>Compare Favorites</h1>
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
                            id="nav-comp-button"
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
                    <form onSubmit={(e) => { e.preventDefault(); handleSearch(); }} className="form-inline d-flex justify-content-between align-items-center">
                        <img src={logo} alt="logo" style={{ height: '75px', marginRight: '20px' }} />
                        <input
                            id = "search-bar"
                            type="text"
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="form-control mr-sm-2"
                            placeholder="Search user or group"
                            style={{ margin: '0 5px', flex: '1' }}
                        />
                        <button type="submit" className="btn" style={{
                            padding: '6px 12px',
                            background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                            color: 'white',
                            border: 'none'
                        }} id="search-button-input">Search</button>
                    </form>
                </div>
            </div>
            {entityNotFound && (
                <div id="not-found" style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    height: '100vh',
                    color: 'black',
                    textAlign: 'center',
                    marginTop: '-20vh'
                }}>
                    This user or group does not exist.
                </div>
            )}
            {selectedEntity && (
                <div style={{ display: 'flex', justifyContent: 'center', padding: '20px' }}>
                    <div style={{
                        width: '40%',
                        margin: '20px',
                        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                        borderRadius: '10px',
                        overflow: 'hidden',
                        textAlign: 'center',
                        background: '#fff',
                    }}>
                        <div style={{ padding: '10px' }}>
                            <h3 id = "user">{selectedEntity.username || selectedEntity.groupName || selectedEntity.members.join(', ')}</h3> {/* This will show the usernames */}
                            <button id = "compare-button-input" onClick={handleCompare} style={{
                                padding: '6px 12px',
                                background: 'linear-gradient(45deg, rgb(33, 46, 53), rgb(128, 145, 54))',
                                color: 'white',
                                border: 'none',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '16px'
                            }}>
                                Compare
                            </button>
                        </div>
                    </div>
                </div>
            )}
            <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', padding: '0 5%' }}>
                {comparisonResults.map((result, index) => (
                    <div
                        key={index}
                        className={"compare-result"}
                        data-testid={`park-card-${result.parkCode}`} // Adding a data-testid attribute
                        style={{
                            width: '40%',
                            margin: '20px',
                            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                            borderRadius: '10px',
                            overflow: 'hidden',
                            textAlign: 'center',
                            background: '#fff',
                        }}>
                        <div style={{ padding: '10px' }}>
                            {result.imageUrl && (
                                <img
                                    src={result.imageUrl}
                                    alt={result.parkName}
                                    style={{
                                        width: '100%',
                                        height: '200px',
                                        objectFit: 'cover',
                                        borderRadius: '10px 10px 0 0'
                                    }}
                                />
                            )}
                            <h3 onClick={() => handleParkClick(result.parkCode)} style={{ cursor: 'pointer' }}>
                                {result.parkName}
                            </h3>
                            <p onClick={() => handleCardVisibility(result.parkCode)} style={{ cursor: 'pointer' }}
                            className={"result-ratio"}>
                                {result.ratio}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
            {visibleCard && (
                <div className={"visible-card"}
                    style={{
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: '300px',
                    backgroundColor: 'white',
                    boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)',
                    borderRadius: '8px',
                    zIndex: 1000,
                }}>
                    <div style={{ padding: '20px', borderBottom: '1px solid #ccc', textAlign: 'center' }}>
                        <strong>{visibleCard.parkName}</strong>
                        <p>Favorited by: {visibleCard.favoritedBy.join(', ')}</p>
                        <button
                            data-testid="close-card-button"
                            className={"close-card-button"}
                            onClick={handleCloseCard}
                            style={{
                                position: 'absolute',
                                top: '10px',
                                right: '10px',
                                cursor: 'pointer',
                                background: 'none',
                                border: 'none',
                                fontSize: '16px',
                            }}>×</button>
                    </div>
                </div>
            )}
            {selectedPark && (
                <div style={{
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: '800px',
                    maxHeight: '90vh',
                    overflowY: 'scroll',
                    backgroundColor: 'white',
                    boxShadow: '0px 0px 10px rgba(0, 0, 0, 0.1)',
                    borderRadius: '8px',
                    zIndex: 1000,
                }}>
                    <div style={{ padding: '20px', borderBottom: '1px solid #ccc', textAlign: 'center' }}>
                        <strong style={{ display: 'block', fontSize: '24px', margin: '0 0 10px' }}>{selectedPark.fullName}</strong> {}
                        <img
                            src={selectedPark.images[0]?.url}
                            alt={selectedPark.images[0]?.altText || 'Park image'}
                            style={{ width: 'calc(100% - 40px)', margin: '20px', borderRadius: '10px' }} // Adjusts image width and margin
                        />
                        <p id={"park-description"}>{selectedPark.description}</p>
                        <p id={"park-location"}>Location: {selectedPark.addresses[0]?.city}, {selectedPark.states}</p>
                        <p id={"park-url"}>URL: <a href={selectedPark.url} target="_blank" rel="noopener noreferrer">{selectedPark.url}</a></p>
                        <p id={"entrance-fee"}>Entrance Fee: {selectedPark.entranceFees.length > 0 ? `$${selectedPark.entranceFees[0].cost}` : 'Free'}</p>
                        <p id={"amenities"}>Amenities: {selectedPark.topics.map(topic => topic.name).join(', ')}</p>
                        <p id={"activities"}>Activities: {selectedPark.activities && selectedPark.activities.map(activity => activity.name).join(', ')}</p>
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
                            }}>Back to Search Results</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CompareFavoritesPage;